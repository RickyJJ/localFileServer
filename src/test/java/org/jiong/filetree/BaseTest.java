package org.jiong.filetree;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

/***
 * Created by DEV049104 on 2019/12/27.
 */
public class BaseTest {
    public static void main(String[] args) throws IOException {
        URL resources = BaseTest.class.getClassLoader().getResource("custom.properties");
        System.out.println("Resources url:");
        System.out.println(resources);

        String properties = System.getProperty("java.class.path");
        System.out.println(properties);
    }
}
