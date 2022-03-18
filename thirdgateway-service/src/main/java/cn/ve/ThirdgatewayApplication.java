package cn.ve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"cn.ve.thirdgateway.dal.mapper"})
@EnableFeignClients
@SpringBootApplication
//@EnableDiscoveryClient // 开启注册发现
@EnableTransactionManagement // 开启事务
public class ThirdgatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThirdgatewayApplication.class, args);
    }
}
