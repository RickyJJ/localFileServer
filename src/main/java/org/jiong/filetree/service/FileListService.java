package org.jiong.filetree.service;

import org.jiong.filetree.model.FileItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

/***
 *
 * @author DEV049104
 * @date 2019/12/25
 */
public interface FileListService {
    int ASCEND = 0x001;
    int DESCEND = 0x002;

    /**
     * 展示目录下的文件，以指定的文件修改时间顺序，默认降序
     * @param dirPath sub files of dir to show
     * @param order ASCEND&DESCEND determine ordering style of files
     * @return sorted files
     */
    List<FileItem> listFiles(String dirPath, int order);

    /**
     * 返回新的dir path,不包含root path
     * @param dirPath
     * @return
     */
    String newDirPath(String dirPath);

    /**
     * @param newDirPath
     * @return
     */
    String getParentPath(String newDirPath);
}
