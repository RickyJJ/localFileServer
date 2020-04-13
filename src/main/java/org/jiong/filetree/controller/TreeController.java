package org.jiong.filetree.controller;

import lombok.extern.slf4j.Slf4j;
import org.jiong.filetree.common.DirectoryProperties;
import org.jiong.filetree.model.FileItem;
import org.jiong.filetree.model.Result;
import org.jiong.filetree.service.ClientService;
import org.jiong.filetree.service.FileListService;
import org.jiong.filetree.service.impl.ClientServiceImpl;
import org.jiong.filetree.token.HandleToken;
import org.jiong.filetree.user.User;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
@Controller
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
    public String goTreePage() {
        return redirectTo("/files");
    }

    @RequestMapping("/files")
    public String listFiles() {
        String dir = directoryProperties.getDir();

        log.debug("dir: {}", dir);
        List<FileItem> fileList = fileListService.listFiles(dir, FileListService.DESCEND);
        setAttr("files", fileList);
        setAttr("isRoot", Boolean.TRUE);
        return "main";
    }

    @RequestMapping("/files/**")
    public String goDir(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        String dirPath = requestURI.substring("/files/".length());
        if (StringUtils.isEmpty(dirPath)) {
            log.warn("dir path is empty, use default");
            return redirectTo("/");
        }

        String newDirPath = fileListService.newDirPath(dirPath);
        if (newDirPath.length() == 0) {
            redirectTo("/");
        }

        List<FileItem> fileItems = fileListService.listFiles(directoryProperties.getDir() + File.separator + newDirPath, FileListService.DESCEND);
        setAttr("files", fileItems);

        setAttr("parentPath", fileListService.getParentPath(newDirPath));
        setAttr("isRoot", Boolean.FALSE);
        return "main";
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

    @RequestMapping("/download/**")
    public Object downloadFile(HttpServletRequest request) throws UnsupportedEncodingException {
        String filePath = request.getRequestURI().substring("/download/".length());
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

        HandleToken userToken = currentUser.getToken();
        log.info("ready for applying token for user: {}", currentUser.getName());

        if (userToken == null || !userToken.isAvailable()) {
            log.info("User [{}] has not token or valid token, now create new token", currentUser.getName());

            Result result = clientService.applyToken(currentUser);
            if (result.isOk()) {
                log.info("Get token key.");
                setSessionAttr("tokenKey", result.get("key"));
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
            return Result.ok();
        }
    }

    @RequestMapping("/token/check")
    public Result userCheckAndFetchToken() {
        User currentUser = getCurrentUser();

        String tokenKey = (String) getSessionAttr("tokenKey");
        if (tokenKey == null) {
            return Result.fail("There is no key to fetch token.");
        } else {
            Result result = clientService.fetchToken(currentUser, tokenKey);

            if (result.isOk()) {
                log.info("Get token.");
                String token = (String) result.get("token");
                currentUser.updateToken(token);
            } else {
                log.warn("Fetching token failed. code: {}, msg: {}", result.getFlag(), result.getMessage());
            }

            setSessionAttr("tokenKey", null);

            return result;
        }

    }
}
