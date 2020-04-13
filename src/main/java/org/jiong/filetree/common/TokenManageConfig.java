package org.jiong.filetree.common;

import lombok.Data;
import org.jiong.filetree.common.factory.MyPropertiesSourceFactory;
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
