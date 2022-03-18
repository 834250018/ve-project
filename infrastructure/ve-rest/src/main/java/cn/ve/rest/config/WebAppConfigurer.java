package cn.ve.rest.config;

import cn.ve.rest.filter.HandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor handlerInterceptor() {
        return new HandlerInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor())
            .excludePathPatterns("/**/fonts/*", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.gif", "/**/*.jpg",
                "/**/*.jpeg", "/error");
    }
}
	