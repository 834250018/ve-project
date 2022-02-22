package cn.ve.message;

import cn.ve.message.dal.mapper.MessageMessageMapper;
import cn.ve.message.dal.mapper.MessageMessageTemplateMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

@MapperScan({"cn.ve.message.dal.mapper"})
@SpringBootApplication
@EnableTransactionManagement // 开启事务
//@EnableDiscoveryClient // 开启注册发现
//@EnableFeignClients(basePackages = {"cn.ve"})
public class MessageApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void run(String... args) throws Exception {
    }

}
