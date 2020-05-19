package localfileserver.client.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***
 *
 * @author Administrator
 * @date 2020/5/19
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/files")
                .allowedOrigins("http://localhost:8081")
                .allowCredentials(true).maxAge(3600);
        registry.addMapping("/token/**")
                .allowedOrigins("http://localhost:8081")
                .allowCredentials(true).maxAge(3600);
    }
}
