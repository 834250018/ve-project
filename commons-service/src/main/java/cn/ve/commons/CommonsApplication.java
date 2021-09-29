package cn.ve.commons;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"cn.ve.commons.dal.mapper"})
@SpringBootApplication
@EnableTransactionManagement // 开启事务
@EnableDiscoveryClient // 开启注册发现
@EnableFeignClients(basePackages = {"cn.ve"})
public class CommonsApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonsApplication.class, args);
    }
}
