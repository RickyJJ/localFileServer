package org.jiong.filetree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

/***
 *
 * @author DEV049104
 * @date 2019/12/24
 */
@SpringBootApplication
public class AppBoot extends SpringBootServletInitializer {
    private static ApplicationContext appContext;
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return super.configure(builder.sources(AppBoot.class));
    }

    public static ApplicationContext getAppContext() {
        return appContext;
    }

    public static void setAppContext(ApplicationContext appContext) {
        AppBoot.appContext = appContext;
    }

    @PostConstruct
    public void appInit() {
        System.out.println("App init success");
    }

    @PostConstruct
    public void tokenInit() {
        System.out.println("App token init success");
    }

    public static void main(String[] args) {
        appContext = SpringApplication.run(AppBoot.class, args);
    }
}
