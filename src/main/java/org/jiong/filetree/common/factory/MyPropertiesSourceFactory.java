package org.jiong.filetree.common.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/***
 * 提供一种加载外部属性文件的方式，配合PropertySource使用
 * 优先加载jar同级目录下的配置文件，如果未找到则从上一级的config
 * 目录下查找
 * @author DEV049104
 * @date 2019/12/27
 */
public class MyPropertiesSourceFactory implements PropertySourceFactory {
    private static final Logger log = LoggerFactory.getLogger(MyPropertiesSourceFactory.class);

    private static final String PRE_PATH = "./";

    private static final String PRE_PARENT_PATH = "../config/";

    private static final String[] YAML_SUFFIX = {"yaml", "yml"};

    private static final String PROPERTIES = "properties";

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        String filename = resource.getResource().getFilename();

        Objects.requireNonNull(filename, "config file is null");
        log.info("Resource name: {}; resource: {}", name, filename);

        Resource classPathResource = new ClassPathResource(PRE_PATH + filename);
        if (!classPathResource.exists()) {
            log.debug("use parent path to load config files.");
            classPathResource = new FileSystemResource(PRE_PARENT_PATH + filename);
        }

        log.info("file exist: {}, path: {}", classPathResource.exists(), classPathResource.getFile().getPath());

        if (Arrays.stream(YAML_SUFFIX).anyMatch(filename::endsWith)) {
            YamlPropertiesFactoryBean yamlPropertySource = new YamlPropertiesFactoryBean();
            yamlPropertySource.setResources(classPathResource);
            return new PropertiesPropertySource(filename, Objects.requireNonNull(yamlPropertySource.getObject()));
        } else if (filename.endsWith(PROPERTIES)) {
            PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
            propertiesFactoryBean.setLocations(classPathResource);
            return new PropertiesPropertySource(filename, Objects.requireNonNull(propertiesFactoryBean.getObject()));
        } else {
            throw new UnsupportedDataTypeException("Only suffixes of .yaml(.yml) or .properties supported.");
        }
    }
}
