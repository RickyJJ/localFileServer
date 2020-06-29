package localfileserver.client.controller;

import com.google.common.base.Strings;
import localfileserver.client.config.DirectoryProperties;
import localfileserver.client.config.param.Dict;
import localfileserver.client.entity.FileItem;
import localfileserver.client.entity.User;
import localfileserver.client.kit.SessionKit;
import localfileserver.client.service.ClientService;
import localfileserver.client.service.FileListService;
import localfileserver.client.service.impl.ClientServiceImpl;
import localfileserver.common.AppConst;
import localfileserver.kit.DateKit;
import localfileserver.model.Result;
import localfileserver.token.ExpiredHandleToken;
import localfileserver.token.HandleToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

/***
 *
 * @author DEV049104
 * @date 2019/12/24
 */
@RestController
@Slf4j
public class TreeController extends BaseController {
    private final DirectoryProperties directoryProperties;

    private final FileListService fileListService;

    private final ClientService clientService;

    public TreeController(DirectoryProperties directoryProperties, FileListService fileListService, ClientServiceImpl clientService) {
        this.directoryProperties = directoryProperties;
        this.fileListService = fileListService;
        this.clientService = clientService;
    }

    @RequestMapping("/")
    public ModelAndView goTreePage() {
        return new ModelAndView("client");
    }

    @GetMapping("/files")
    public Result listFiles() {
        String dir = directoryProperties.getDir();
        Result result = Result.ok();

        log.debug("dir: {}", dir);
        List<FileItem> fileList = fileListService.listFiles(dir, FileListService.DESCEND);

        result.add("files", fileList);
        result.add("isRoot", Boolean.TRUE);
        return result;
    }


    @RequestMapping("/files/**")
    public Result goDir(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        String prePath = "/files";
        String dirPath = requestURI.substring(requestURI.indexOf(prePath) + prePath.length());
        if (StringUtils.isEmpty(dirPath)) {
            log.warn("dir path is empty, use default");
            dirPath = "/";
        }

        String newDirPath = fileListService.newDirPath(dirPath);
        if (newDirPath.length() == 0) {
            return Result.fail("Wrong path", "Directory path is wrong");
        }

        Result result = Result.ok();
        List<FileItem> fileItems = fileListService.listFiles(directoryProperties.getDir() + File.separator + newDirPath, FileListService.DESCEND);
        result.add("files", fileItems);

        result.add("parentPath", fileListService.getParentPath(newDirPath));
        result.add("isRoot", Boolean.FALSE);
        return result;
    }

    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public String fileUpload(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        if (Objects.isNull(name)) {
            setAttr("error_msg", "Upload file is null");
            return "error";
        }

        log.debug("file name: {}", name);
        File file1 = new File(directoryProperties.getDir(), name);

        try (InputStream inputStream = file.getInputStream()) {

            if (!file1.exists()) {
                boolean newFile = file1.createNewFile();
                log.debug("newFile created: {}", newFile);
            }

            log.info("File path to store: {}", file1.getPath());
            Files.copy(inputStream, Paths.get(file1.toURI()), StandardCopyOption.REPLACE_EXISTING);
            log.info("FileUpload completed.");
        } catch (IOException e) {
            log.info("File upload failed", e);
        }

        return redirectTo("/");
    }

    @GetMapping("/preDownload")
    public Result checkBeforeDownload() {
        User currentUser = getCurrentUser();

        if (currentUser.getToken() != null && currentUser.getToken().isAvailable()) {
            return Result.ok();
        }

        return Result.fail("Download denied, needed for a token.");
    }

    @RequestMapping("/download/**")
    @PreAuthorize("hasAuthority('download')")
    public Object downloadFile(HttpServletRequest request) throws UnsupportedEncodingException {
        String uri = request.getRequestURI();
        String preDownloadUrl = "/download/";
        String filePath = uri.substring(uri.indexOf(preDownloadUrl) + preDownloadUrl.length());
        filePath = URLDecoder.decode(filePath, "UTF-8");

        log.debug("Download Uri: {}", filePath);
        if (StringUtils.isEmpty(filePath)) {
            log.warn("File path is empty.");
            return null;
        }

        File file = new File(directoryProperties.getDir() + File.separatorChar + filePath);
        if (!file.exists()) {
            log.error("File does not exist: {}", file.getPath());
            setAttr("error_msg", "The File has gone");
            return "error";
        }
        log.debug("File to download: {}", file.getPath());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));

        return ResponseEntity.ok().headers(headers).contentLength(file.length()).body(new FileSystemResource(file));
    }

    /**
     * user apply token to be allowed to use app
     * <p>
     * User create a new apply to acquire token
     * if user has normal token, ignore it, or create new apply
     * <p>
     * Just in test, not for use
     *
     * @return json with permit to use app
     */
    @RequestMapping("/token/apply")
    public Result userApplyToken() {
        User currentUser = getCurrentUser();

        String tokenKey = (String) getSessionAttr(Dict.Token.TOKEN_KEY);
        if (tokenKey != null) {
            Result result = clientService.userCheckAndFetchToken(currentUser, tokenKey);
            result.add("hasToken", result.isOk() ? "1" : "0");

            // ready to update user token info
            SessionKit.getSession().setAttribute(Dict.User.UPDATED_FLAG, "1");
            return result;
        }

        HandleToken userToken = currentUser.getToken();
        log.info("ready for applying token for user: {}", currentUser.getName());

        if (userToken == null || !userToken.isAvailable()) {
            log.info("User [{}] has not token or valid token, now create new token", currentUser.getName());

            Result result = clientService.applyToken(currentUser);
            if (result.isOk()) {
                log.info("Get token key.");
                setSessionAttr(Dict.Token.TOKEN_KEY, result.get(AppConst.TOKEN_APPLY_KEY));
            } else {
                log.info("Token apply failed.");
            }

            return result;
        } else if (userToken.isTemporal()) {
            log.info("User [{}] has temporal token , don't need to applying for token", currentUser.getName());
            return Result.ok();
        } else {
            // ignore
            log.info("User has long-term token, don't need to applying for token");
            return Result.fail("1006", "Already has forever token");
        }
    }

    /**
     * 获取用户token值
     * 如果有可用token，则更新用户权限，允许下载文件，否则不允许
     * @return result
     */
    @GetMapping ("/userToken")
    public Result getUserToken() {
        User currentUser = getCurrentUser();
        HandleToken token = currentUser.getToken();

        if (token == null) {
            return Result.fail("1000", "No token");
        } else if (Strings.isNullOrEmpty(token.value()) || !token.isAvailable()) {
            log.warn("user token: {}", token);
            return Result.fail("1001", "No available token");
        }

        Result result = Result.ok();
        String tokenExpireDate = token.isTemporal() ? DateKit.timestamp(((ExpiredHandleToken) token).getExpiredTime()) : "长期";

        result.add("tokenExpireDate", tokenExpireDate);
        result.add("tokenType", token.isTemporal() ? "2" : "1");
        return result;
    }

    @GetMapping("/test")
    public String testMsg() {
        log.info("access to method.");
        return "test message";
    }

}
