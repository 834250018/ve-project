package cn.ve.user;

import cn.hutool.core.codec.Base64Encoder;
import cn.ve.base.util.PasswordUtils;
import cn.ve.user.dal.entity.UserLoginRelation;
import cn.ve.user.dal.entity.UserUser;
import cn.ve.user.dal.mapper.UserLoginRelationMapper;
import cn.ve.user.dal.mapper.UserUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    private UserLoginRelationMapper userLoginRelationMapper;
    @Resource
    private UserUserMapper userUserMapper;
    //    @Resource
    //    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 测试数据库
        //        checkAdminExists("root");
        // 测试redis
        //        redisTemplate.opsForValue().set("1", "2");
    }

    private void checkAdminExists(String username) throws Exception {
        UserLoginRelation qo = new UserLoginRelation();
        List<UserLoginRelation> userLogins = userLoginRelationMapper.selectList(
            new LambdaQueryWrapper<UserLoginRelation>().eq(UserLoginRelation::getUsername, username)
                .eq(UserLoginRelation::getLoginType, 2));
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
        userUserMapper.insert(user);
        // 创建登录数据
        UserLoginRelation userLoginRelation = new UserLoginRelation();
        userLoginRelation.setUserId(user.getId());
        userLoginRelation.setLoginType(2);
        userLoginRelation.setUsername(username);
        userLoginRelation.setPassword(pwd);
        userLoginRelation.setSalt(salt);
        userLoginRelationMapper.insert(userLoginRelation);
    }

}
