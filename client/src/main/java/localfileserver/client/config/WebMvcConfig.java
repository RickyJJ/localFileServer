package localfileserver.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***
 * 设置客户端访问路径
 * @author Administrator
 * @date 2020/5/19
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/files")
                .allowedOrigins("http://localhost:8080")
                .allowCredentials(true).maxAge(3600);
        registry.addMapping("/token/**")
                .allowedOrigins("http://localhost:8080")
                .allowCredentials(true).maxAge(3600);
    }
}
