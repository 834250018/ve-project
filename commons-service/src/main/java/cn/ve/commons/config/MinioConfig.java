package cn.ve.commons.config;

import cn.ve.file.util.MinioUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ve
 * @date 2021/7/28
 */
//@Configuration
public class MinioConfig {

    @Bean
    public MinioUtil minioEngine(@Value("${minio.local-url}") String insideNetwork,
        @Value("${minio.url}") String externalAddress, @Value("${minio.access-key}") String accessKey,
        @Value("${minio.secret-key}") String secretKey) {
        return new MinioUtil(insideNetwork, externalAddress, accessKey, secretKey);
    }

}
