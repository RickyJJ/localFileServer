package org.jiong.filetree;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;

/***
 * Created by DEV049104 on 2019/12/27.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Before()
    public void setAppContext() {
        System.out.println(applicationContext);
        AppBoot.setAppContext(applicationContext);
    }

    public static void main(String[] args) throws IOException {

        URL resources = BaseTest.class.getClassLoader().getResource("custom.properties");
        System.out.println("Resources url:");
        System.out.println(resources);

        String properties = System.getProperty("java.class.path");
        System.out.println(properties);
    }
}
