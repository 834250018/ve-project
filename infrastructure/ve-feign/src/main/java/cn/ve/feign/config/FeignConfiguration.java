package cn.ve.feign.config;

import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author ve
 * @date 2022/3/18
 */
@Configuration
public class FeignConfiguration {

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    public Feign.Builder feignBuilder() {
        return Feign.builder().requestInterceptor(new FeignRequestInterceptor());
    }

}
