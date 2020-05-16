package localfileserver.server;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SpringBootApplication
public class ServerApplication implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    public static ApplicationContext getAppContext() {
        return applicationContext;
    }

    public static void setAppContext(ApplicationContext applicationContext) {
        ServerApplication.applicationContext = applicationContext;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServerApplication.applicationContext = applicationContext;
    }
}
