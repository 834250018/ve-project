package cn.ve.commons.config;

import cn.ve.file.config.MinioEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ve
 * @date 2021/7/28
 */
@Configuration
public class MinioConfig {

    @Bean
    public MinioEngine minioEngine(@Value("${minio.local-url}") String insideNetwork,
        @Value("${minio.url}") String externalAddress, @Value("${file.default-bucket-name}") String defaultBucketName,
        @Value("${minio.access-key}") String accessKey, @Value("${minio.secret-key}") String secretKey) {
        return new MinioEngine(insideNetwork, externalAddress, defaultBucketName, accessKey, secretKey);
    }

}
