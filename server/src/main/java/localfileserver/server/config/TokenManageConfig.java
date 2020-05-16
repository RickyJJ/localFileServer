package localfileserver.server.config;

import localfileserver.factory.MyPropertiesSourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Mr.Jiong
 */
@Component
@PropertySource(value = "classpath:tokenManager.yml", factory = MyPropertiesSourceFactory.class)
@ConfigurationProperties(prefix = "token")
@Data
public class TokenManageConfig {
    private String adminIp;

    private String tokenPath;
}
