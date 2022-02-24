package cn.ve.thirdgateway.config;

import cn.ve.thirdgateway.service.AliSMS;
import cn.ve.thirdgateway.service.BaseSMSEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ve
 * @date 2022/2/24
 */
@Configuration
public class SmsConfig {
    @Bean
    public BaseSMSEngine baseSMSEngine(@Value("${ali.sms.access-key-id}") String accessKeyId,
        @Value("${ali.sms.access-key-secret}") String accessKeySecret) {
        return new AliSMS(accessKeyId, accessKeySecret);
    }
}
