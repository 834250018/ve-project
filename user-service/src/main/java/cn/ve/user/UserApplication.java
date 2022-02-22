package cn.ve.user;

import cn.hutool.core.codec.Base64Encoder;
import cn.ve.base.util.PasswordUtils;
import cn.ve.user.dal.entity.UserLogin;
import cn.ve.user.dal.entity.UserUser;
import cn.ve.user.dal.mapper.ext.UserLoginExtMapper;
import cn.ve.user.dal.mapper.ext.UserUserExtMapper;
import cn.ve.user.param.SystemAlertMqParam;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;

@MapperScan({"cn.ve.user.dal.mapper"})
@SpringBootApplication
@EnableTransactionManagement // 开启事务
//@EnableDiscoveryClient // 开启注册发现
//@EnableFeignClients(basePackages = {"cn.ve"})
public class UserApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @Resource
    private UserLoginExtMapper userLoginExtMapper;
    @Resource
    private UserUserExtMapper userUserExtMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 测试数据库
        checkAdminExists("root");
        // 测试mq
        SystemAlertMqParam systemAlertMqParam = new SystemAlertMqParam(rabbitTemplate);
        systemAlertMqParam.setUserId(0L);
        systemAlertMqParam.sendMessage();
        // 测试redis
        redisTemplate.opsForValue().set("1", "2");
    }

    private void checkAdminExists(String username) throws Exception {
        UserLogin qo = new UserLogin();
        qo.setUsername(username);
        qo.setLoginType(2);
        List<UserLogin> userLogins = userLoginExtMapper.queryAll(qo);
        if (!CollectionUtils.isEmpty(userLogins)) {
            return;
        }
        // 前端摘要
        byte[] bytes = "ve1`>3.".getBytes(StandardCharsets.UTF_8);
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(bytes);
        String encryptedPwd = Base64Encoder.encode(digest);
        // 后端摘要
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("salt" + salt);
        System.out.println("encryptedPwd" + encryptedPwd);
        String pwd = PasswordUtils.genPwdCiphertext(encryptedPwd, salt);
        System.out.println(pwd);
        // 创建管理员数据
        UserUser user = new UserUser();
        user.setNickname("init");
        userUserExtMapper.insert(user);
        // 创建登录数据
        UserLogin userLogin = new UserLogin();
        userLogin.setUserId(user.getId());
        userLogin.setLoginType(2);
        userLogin.setUsername(username);
        userLogin.setPassword(pwd);
        userLogin.setSalt(salt);
        userLoginExtMapper.insert(userLogin);
    }

}
