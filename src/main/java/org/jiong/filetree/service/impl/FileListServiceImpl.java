package org.jiong.filetree.service.impl;

import org.jiong.filetree.common.DirectoryProperties;
import org.jiong.filetree.model.FileItem;
import org.jiong.filetree.service.FileListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/***
 *
 * @author DEV049104
 * @date 2019/12/25
 */
@Service
public class FileListServiceImpl implements FileListService {
    private static final Logger log = LoggerFactory.getLogger(FileListServiceImpl.class);

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final static NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());

    @Resource
    private DirectoryProperties directoryProperties;

    @Override
    public List<FileItem> listFiles(String dirPath, int order) {
        Objects.requireNonNull(dirPath, "dir path is null");

        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            throw new NullPointerException("Dir does not exist.");
        }

        if (order != FileListService.ASCEND && order != FileListService.DESCEND) {
            log.warn("The order flag is wrong, use default");
            order = FileListService.DESCEND;
        }

        File[] files = dirFile.listFiles();

        if (files == null) {
            log.warn("Sub files is empty of this dir: {}", dirPath);
            return new ArrayList<>();
        }

        int finalOrder = order;
        return Arrays.stream(files).sorted((f1, f2) -> compareFileLastModified(finalOrder, f1.lastModified(), f2.lastModified()))
                .map(this::createItem).collect(Collectors.toList());
    }

    private int compareFileLastModified(int order, long fl1, long fl2) {
        if (order == FileListService.ASCEND) {
            return Long.compare(fl1, fl2);
        } else {
            return Long.compare(fl2, fl1);
        }
    }

    @Override
    public String newDirPath(String dirPath) {
        return isValidDir(dirPath) ? dirPath : "";
    }

    private boolean isValidDir(String dirPath) {
        if (StringUtils.isEmpty(dirPath)) {
            log.warn("dir path is empty");
            return false;
        }

        String filePath = directoryProperties.getDir() + File.separatorChar + dirPath;
        log.debug("Validate file path: {}", filePath);

        FileSystemResource fileSystemResource = new FileSystemResource(filePath);
        return fileSystemResource.exists();
    }

    @Override
    public String getParentPath(String newDirPath) {
        log.debug("relative path: {}", newDirPath);
        String clearedPath = getClearPath(newDirPath);

        String slash = "/";
        clearedPath = clearedPath.startsWith(slash) ? clearedPath.substring(1) : clearedPath;
        if (clearedPath.contains(slash)) {
            return clearedPath.substring(0, clearedPath.lastIndexOf(slash));
        } else {
            return "";
        }
    }

    private String getClearPath(String path) {
        return path.replaceAll("\\\\", "/");
    }

    private FileItem createItem(File file) {
        FileItem fileItem = new FileItem();

        fileItem.setName(file.getName());
        fileItem.setPath(file.getPath().substring(directoryProperties.getDir().length() + 1));
        fileItem.setDir(file.isDirectory());

        if (fileItem.isDir()) {
            fileItem.setSize("");
        } else {
            numberFormat.setMaximumFractionDigits(2);
            if (file.length() >> 20 > 1) {
                fileItem.setSize(numberFormat.format(file.length() / (double) (1 << 20)) + "Mb");
            } else {
                fileItem.setSize(numberFormat.format((file.length() / (double) (1 << 10))) + "Kb");
            }
        }

        Date date = new Date(file.lastModified());
        LocalDateTime localDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        fileItem.setLastModifiedDate(localDate.format(dateTimeFormatter));

        return fileItem;
    }
}
