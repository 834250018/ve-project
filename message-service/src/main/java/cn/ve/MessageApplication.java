package cn.ve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

@MapperScan({"cn.ve.message.dal.mapper"})
@SpringBootApplication
@EnableTransactionManagement // 开启事务
//@EnableDiscoveryClient // 开启注册发现
@EnableFeignClients
public class MessageApplication implements CommandLineRunner {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }

}
