package cn.ve.thirdgateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan({"cn.ve.thirdgateway.dal.mapper"})
@SpringBootApplication
@EnableTransactionManagement // 开启事务
//@EnableDiscoveryClient // 开启注册发现
//@EnableFeignClients(basePackages = {"cn.ve"})
public class ThirdgatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThirdgatewayApplication.class, args);
    }
}
