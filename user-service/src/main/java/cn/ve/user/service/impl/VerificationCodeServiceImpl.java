package cn.ve.user.service.impl;

import cn.ve.base.pojo.VeException;
import cn.ve.base.util.StringConstant;
import cn.ve.message.api.param.SmsMqParam;
import cn.ve.user.service.VerificationCodeService;
import cn.ve.user.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * todo 发短信改成同步
 *
 * @author ve
 * @date 2021/8/9
 */
@Slf4j
@Service
@RefreshScope
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Value("${switch.access-sms-code:false}")
    private Boolean accessCodeSwitch;
    @Value("${access-sms-code:986654}")
    private String accessCode;

    private static final String TEMPLATE_CODE_VERIFICATION_CODE = "";
    private static final String TEMPLATE_CODE_VERIFICATION_CODE_PARAM_CODE = "";
    private static final String SIGN_NAME_1 = "";

    @Override
    public void sendSMSCode(String prefixType, String phoneNo) {
        // 先检查手机号码发送频率
        checkFrequency(prefixType + StringConstant.UNDERLINE + phoneNo);
        // 存入redis
        String code = RandomUtils.nextSmsCode();

        redisTemplate.opsForValue().set(prefixType + StringConstant.UNDERLINE + phoneNo, code, 5, TimeUnit.MINUTES);
        // 发送短信
        sendMsg(phoneNo, code);
    }

    private void sendMsg(String phoneNo, String code) {
        SmsMqParam smsMqParam = new SmsMqParam(amqpTemplate);
        smsMqParam.setSignName(SIGN_NAME_1);
        smsMqParam.setPhoneNumbers(phoneNo);
        smsMqParam.setTemplateCode(TEMPLATE_CODE_VERIFICATION_CODE);
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put(TEMPLATE_CODE_VERIFICATION_CODE_PARAM_CODE, code);
        smsMqParam.setParamMap(paramMap);
        smsMqParam.sendMessage();
    }

    private void checkFrequency(String key) {
        Boolean ok = redisTemplate.opsForValue().setIfAbsent("sms_frequency_" + key, "0", 1, TimeUnit.MINUTES);
        if (ok == null) {
            throw new VeException(400, "操作太频繁");
        }
        if (!ok) {
            throw new VeException(400, "操作太频繁");
        }
    }

    @Override
    public void checkSMSCodeAndRemove(String prefixType, String phoneNo, String code) {
        if (accessCodeSwitch && code.equals(accessCode)) {
            return;
        }
        checkSMSCode(prefixType, phoneNo, code);
        // 作废验证码
        redisTemplate.delete(prefixType + StringConstant.UNDERLINE + phoneNo);
    }

    @Override
    public void checkSMSCode(String prefixType, String phoneNo, String code) {
        if (accessCodeSwitch && code.equals(accessCode)) {
            return;
        }
        // 校验验证码
        String sendCode = redisTemplate.opsForValue().get(prefixType + StringConstant.UNDERLINE + phoneNo);
        if (StringUtils.isBlank(sendCode) || !sendCode.equals(code)) {
            throw new VeException("验证码错误");
        }
    }
}
