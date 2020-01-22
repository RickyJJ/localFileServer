package org.jiong.filetree.common;

import lombok.Getter;
import org.jiong.filetree.common.factory.MyPropertiesSourceFactory;
import org.springframework.beans.factory.annotation.Value;
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
public class DirectoryProperties {

    @Getter
    private String dir;

    @Getter
    private String userPath;

}
