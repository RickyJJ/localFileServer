package org.jiong.filetree.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jiong.filetree.common.factory.MyPropertiesSourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/***
 *
 * @author DEV049104
 * @date 2019/12/25
 */
@Component("fileConfig")
@PropertySource(value = "classpath:custom.yml", factory = MyPropertiesSourceFactory.class)
@ConfigurationProperties(prefix = "file")
@Data
public class DirectoryProperties {

    @Getter @Setter
    private String dir;

    @Getter @Setter
    private String userPath;

}
