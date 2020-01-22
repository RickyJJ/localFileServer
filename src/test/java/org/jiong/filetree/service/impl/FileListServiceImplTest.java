package org.jiong.filetree.service.impl;

import org.jiong.filetree.service.FileListService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/***
 * Created by DEV049104 on 2019/12/25.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileListServiceImplTest {

    @Autowired
    private FileListService fileListService;
    @Test
    public void dummy1() {
        File jarFile = new File("D:\\HjtWorkSpace\\Projects\\git\\fileTreeUploader\\enjoy-3.7.jar");
        long l = jarFile.lastModified();

        Date date = new Date(l);
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(localDateTime.format(dateTimeFormatter));
    }

    @Test
    public void testFilePath() {
        File file = new File("D:\\HjtWorkSpace\\Projects\\git\\fileTreeUploader\\enjoy-3.7.jar\n");
        String substring = file.getPath().substring("D:\\HjtWorkSpace\\Projects".length() + 1);
        System.out.println(substring);

    }

    @Test
    public void locationTest() {
        URI uri = URI.create("/error.html");
        System.out.println(uri);
    }

    @Test
    public void getParentPathTest() {
        String parentPath = fileListService.getParentPath("D:/files/upload/test/file/test");
        Assert.assertEquals(parentPath, "file");
    }

    @Test
    public void getParentPathInWinTest() {
        String parentPath = fileListService.getParentPath("D:\\files/upload\\test/file/test");
        Assert.assertEquals(parentPath, "file");
    }
}
