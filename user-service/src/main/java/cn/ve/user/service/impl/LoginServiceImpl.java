package cn.ve.user.service.impl;

import cn.ve.base.constant.LoginTypeEnum;
import cn.ve.base.pojo.VeException;
import cn.ve.base.util.BeanUtils;
import cn.ve.base.util.PasswordUtils;
import cn.ve.base.util.StringConstant;
import cn.ve.feign.pojo.CommonResult;
import cn.ve.thirdgateway.api.ThirdgatewayApi;
import cn.ve.thirdgateway.pojo.WechatOpenidDTO;
import cn.ve.user.constant.RedisPrefixTypeConstant;
import cn.ve.user.dal.entity.UserLoginRelation;
import cn.ve.user.dal.entity.UserUser;
import cn.ve.user.dal.mapper.UserLoginRelationMapper;
import cn.ve.user.dal.mapper.UserUserMapper;
import cn.ve.user.dto.LoginByWechatDTO;
import cn.ve.user.dto.UserLoginRelationDTO;
import cn.ve.user.dto.WechatOepnidWithNameDTO;
import cn.ve.user.service.LoginService;
import cn.ve.user.service.VerificationCodeService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ve
 * @date 2021/8/2
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private ThirdgatewayApi thirdgatewayApi;
    @Resource
    private UserLoginRelationMapper userLoginRelationMapper;
    @Resource
    private UserUserMapper userUserMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private VerificationCodeService verificationCodeService;

    @Override
    public void sendLoginCodeMsg(String phone) {
        verificationCodeService.sendSMSCode(RedisPrefixTypeConstant.USER_PHONE_LOGIN, phone);
    }

    @Override
    @Transactional
    public String loginByPhone(String phone, String code, String inviterCode) {
        // 校验验证码
        verificationCodeService.checkSMSCodeAndRemove(RedisPrefixTypeConstant.USER_PHONE_LOGIN, phone, code);
        // 查询用户是否存在
        UserLoginRelation userLoginRelation = userLoginRelationMapper.selectOne(
            new LambdaQueryWrapper<UserLoginRelation>()
                .eq(UserLoginRelation::getLoginType, LoginTypeEnum.PHONE.getCode())
                .eq(UserLoginRelation::getUsername, phone));
        if (userLoginRelation == null) {
            // 走注册登录
            UserUser user = newUser(phone);
            userUserMapper.insert(user);

            userLoginRelation = new UserLoginRelation();
            userLoginRelation.setUserId(user.getId());
            userLoginRelation.setLoginType(LoginTypeEnum.PHONE.getCode());
            userLoginRelation.setUsername(phone);
            userLoginRelationMapper.insert(userLoginRelation);
        }
        // 存储用户并返回token
        return user2Token(userLoginRelation);
    }

    private UserUser newUser(String phone) {
        UserUser user = new UserUser();
        user.setPhone(phone);
        user.setGender(1);
        return user;
    }

    @Override
    public LoginByWechatDTO loginByWechat(String jscode, String headPortrait, String realName) {
        WechatOpenidDTO openidByJscode = getOpenidByJscode(jscode);
        if (openidByJscode.getErrcode() != null && !openidByJscode.getErrcode().equals(0)) {
            log.error("请求第三方网关异常_{}", openidByJscode.getErrmsg());
            throw new VeException("微信请求异常");
        }
        // 查询用户是否存在
        int loginType = LoginTypeEnum.WECHAT.getCode();
        UserLoginRelation userLoginRelation = userLoginRelationMapper.selectOne(
            new LambdaQueryWrapper<UserLoginRelation>().eq(UserLoginRelation::getLoginType, loginType)
                .eq(UserLoginRelation::getUsername, openidByJscode.getOpenid()));
        LoginByWechatDTO result = new LoginByWechatDTO();
        if (userLoginRelation != null) {
            result.setLoggedIn(Boolean.TRUE);
            result.setLoginToken(user2Token(userLoginRelation));
            // 存储用户并返回token
            return result;
        }
        // 如果没有账号则返回一个返回前端走关联手机号注册
        String uuid = UUID.randomUUID().toString().replace("-", "");
        WechatOepnidWithNameDTO openidByJscodeWithName = BeanUtils.copy(openidByJscode, WechatOepnidWithNameDTO.class);
        openidByJscodeWithName.setRealName(realName);
        openidByJscodeWithName.setHeadPortrait(headPortrait);
        redisTemplate.opsForValue()
            .set(RedisPrefixTypeConstant.USER_WECHAT_BIND_PHONE + uuid, JSON.toJSONString(openidByJscodeWithName), 5,
                TimeUnit.MINUTES);
        result.setLoggedIn(Boolean.FALSE);
        result.setTempToken(uuid);
        return result;

    }

    @Override
    public String loginOnlyByWechat(String jscode) {
        WechatOpenidDTO openidByJscode = getOpenidByJscode(jscode);
        if (openidByJscode.getErrcode() != null && !openidByJscode.getErrcode().equals(0)) {
            log.error("请求第三方网关异常_{}", openidByJscode.getErrmsg());
            throw new VeException("服务器异常");
        }
        // 查询用户是否存在
        int loginType = LoginTypeEnum.WECHAT.getCode();
        UserLoginRelation userLoginRelation = userLoginRelationMapper.selectOne(
            new LambdaQueryWrapper<UserLoginRelation>().eq(UserLoginRelation::getLoginType, loginType)
                .eq(UserLoginRelation::getUsername, openidByJscode.getOpenid()));
        if (userLoginRelation == null) {
            throw new VeException("账号不存在");
        }
        // 如果数据库没有unionId,但第三方有,则更新unionId
        unionIdRelate(openidByJscode, userLoginRelation);
        // 存储用户并返回token
        return user2Token(userLoginRelation);
    }

    private void unionIdRelate(WechatOpenidDTO openidByJscode, UserLoginRelation qo) {
        if (StringUtils.isNotBlank(qo.getPassword()) || StringUtils.isBlank(openidByJscode.getUnionid())) {
            return;
        }
        UserLoginRelation update = new UserLoginRelation();
        update.setId(qo.getId());
        update.setPassword(openidByJscode.getUnionid());
        userLoginRelationMapper.updateById(update);
    }

    @Override
    public WechatOpenidDTO getOpenidByJscode(String jscode) {
        CommonResult<WechatOpenidDTO> openidByJscode = thirdgatewayApi.getOpenidByJscode(jscode);
        if (openidByJscode.getCode() != 200) {
            log.error("请求第三方网关异常_{}", openidByJscode.getMsg());
            throw new VeException("服务器异常");
        }
        return openidByJscode.getData();
    }

    @Override
    @Transactional
    public String bindPhoneWithWechat(String uuid, String encryptedData, String iv, String inviterCode) {
        String json = redisTemplate.opsForValue().get(RedisPrefixTypeConstant.USER_WECHAT_BIND_PHONE + uuid);
        WechatOepnidWithNameDTO openidByJscode = JSON.parseObject(json, WechatOepnidWithNameDTO.class);
        String phone = thirdgatewayApi.getPhoneByEncryptedData(encryptedData, openidByJscode.getSession_key(), iv);
        // 通过手机号查询登录方式,如果有,新增微信登录,如果没有新增账号,再新增手机号登录跟微信登录
        UserLoginRelation userLoginRelation = userLoginRelationMapper.selectOne(
            new LambdaQueryWrapper<UserLoginRelation>()
                .eq(UserLoginRelation::getLoginType, LoginTypeEnum.PHONE.getCode())
                .eq(UserLoginRelation::getUsername, phone));
        UserUser user;
        if (userLoginRelation == null) {
            // 新增用户
            user = newUser(phone);
            user.setNickname(openidByJscode.getRealName());
            user.setHeadPortrait(openidByJscode.getHeadPortrait());
            userUserMapper.insert(user);
            userLoginRelation = new UserLoginRelation();
            userLoginRelation.setLoginType(LoginTypeEnum.PHONE.getCode());
            userLoginRelation.setUsername(phone);
            userLoginRelation.setUserId(user.getId());
            // 新增手机号登录方式
            userLoginRelationMapper.insert(userLoginRelation);
            //copyToOtherSide(qo);
        }
        // 新增微信登录方式
        UserLoginRelation wechatLoginRelation = new UserLoginRelation();
        int loginType = LoginTypeEnum.WECHAT.getCode();
        wechatLoginRelation.setLoginType(loginType);
        wechatLoginRelation.setUsername(openidByJscode.getOpenid());
        wechatLoginRelation.setPassword(openidByJscode.getUnionid());
        wechatLoginRelation.setUserId(userLoginRelation.getUserId());
        userLoginRelationMapper.insert(wechatLoginRelation);

        // 存储用户并返回token
        return user2Token(wechatLoginRelation);

    }

    /**
     * 登录并获取token
     *
     * @param loginRelation
     * @return
     */
    private String user2Token(UserLoginRelation loginRelation) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 删除上一次登录token
        String lastToken = redisTemplate.opsForValue()
            .get(RedisPrefixTypeConstant.USER_LAST_USER_TOKEN + StringConstant.UNDERLINE + loginRelation.getUserId());
        redisTemplate.delete(RedisPrefixTypeConstant.USER_LOGGED_IN_TOKEN + lastToken);
        UserLoginRelationDTO userLoginRelationDTO = BeanUtils.copy(loginRelation, UserLoginRelationDTO.class);

        UserUser user = userUserMapper.selectById(userLoginRelationDTO.getUserId());
        // 创建新token
        redisTemplate.opsForValue()
            .set(RedisPrefixTypeConstant.USER_LOGGED_IN_TOKEN + token, JSON.toJSONString(userLoginRelationDTO), 30,
                TimeUnit.DAYS);
        redisTemplate.opsForValue()
            .set(RedisPrefixTypeConstant.USER_LAST_USER_TOKEN + StringConstant.UNDERLINE + loginRelation.getUserId(),
                token, 30, TimeUnit.DAYS);
        return token;
    }

    @Override
    public void removeToken() {
        // 创建新token
        Long userId = getUserId();
        String token = redisTemplate.opsForValue()
            .get(RedisPrefixTypeConstant.USER_LAST_USER_TOKEN + StringConstant.UNDERLINE + userId);
        if (StringUtils.isBlank(token)) {
            return;
        }
        redisTemplate.delete(RedisPrefixTypeConstant.USER_LAST_USER_TOKEN + StringConstant.UNDERLINE + userId);
        redisTemplate.delete(RedisPrefixTypeConstant.USER_LOGGED_IN_TOKEN + token);

    }

    private Long getUserId() {
        // todo
        return null;
    }

    @Override
    public String loginByPassword(String username, String password) {
        // 查询用户是否存在
        UserLoginRelation userLoginRelation = userLoginRelationMapper.selectOne(
            new LambdaQueryWrapper<UserLoginRelation>()
                .eq(UserLoginRelation::getLoginType, LoginTypeEnum.PASSWORD.getCode())
                .eq(UserLoginRelation::getUsername, username));
        if (userLoginRelation == null) {
            throw new VeException(401, "用户不存在");
        }
        // 检查密码是否正确
        if (!PasswordUtils.genPwdCiphertext(password, userLoginRelation.getSalt())
            .equals(userLoginRelation.getPassword())) {
            throw new VeException(401, "用户不存在");
        }
        // 存储用户并返回token
        return user2Token(userLoginRelation);
    }

}
