package localfileserver.server;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;
import localfileserver.server.service.impl.ServerServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Administrator
 */
@SpringBootApplication
@EnableDubbo(scanBasePackages = {"localfileserver.server.service.impl"})
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
