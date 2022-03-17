package cn.ve.user.controller;

import cn.ve.base.pojo.VeException;
import cn.ve.rest.pojo.BaseController;
import cn.ve.user.constant.RedisPrefixTypeConstant;
import cn.ve.user.dto.LoginByWechatDTO;
import cn.ve.user.dto.UserLoginRelationDTO;
import cn.ve.user.service.LoginService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.concurrent.TimeUnit;

/**
 * 公开控制器
 *
 * @author ve
 * @date 2021/8/2
 */
@RequestMapping("/public")
@RestController
@Validated
@Slf4j
public class PublicController extends BaseController {

    @Resource
    RedisTemplate<String, String> redisTemplate;
    @Resource
    LoginService loginService;

    /**
     * 手机号码登录-发送登录验证码
     *
     * @param phone
     * @return
     */
    @PostMapping("/v1.0/sendLoginCodeMsg")
    public String sendLoginCodeMsg(@RequestParam @NotBlank @Pattern(regexp = "^\\d{11}$") String phone) {
        loginService.sendLoginCodeMsg(phone);
        return "ok";
    }

    /**
     * 手机号码登录
     *
     * @param phone
     * @param code
     * @param inviterCode
     * @param response
     * @return
     */
    @PostMapping("/v1.0/loginByPhone")
    public String loginByPhone(
        @RequestParam @NotBlank @Pattern(regexp = "^\\d{11}$", message = "请输入正确的手机号") String phone,
        @RequestParam @NotBlank String code, @RequestParam(required = false) String inviterCode,
        HttpServletResponse response) {
        String token = loginService.loginByPhone(phone, code, inviterCode);
        setCookies(response, token);
        return "ok";
    }

    /**
     * 微信登录并注册
     *
     * @param jscode
     * @param headPortrait
     * @param realName
     * @param response
     * @return
     */
    @PostMapping("/v1.0/loginByWechat")
    public LoginByWechatDTO loginByWechat(@RequestParam @NotBlank String jscode,
        @RequestParam @NotBlank String headPortrait, @NotBlank @RequestParam String realName,
        HttpServletResponse response) {
        LoginByWechatDTO loginByWechatDTO = loginService.loginByWechat(jscode, headPortrait, realName);
        if (loginByWechatDTO.getLoggedIn()) {
            setCookies(response, loginByWechatDTO.getLoginToken());
        }
        return loginByWechatDTO;
    }

    /**
     * 微信仅登录(静默授权)
     *
     * @param jscode
     * @param response
     * @return
     */
    @PostMapping("/v1.0/loginOnlyByWechat")
    public String loginOnlyByWechat(@RequestParam @NotBlank String jscode, HttpServletResponse response) {
        String loginToken = loginService.loginOnlyByWechat(jscode);
        setCookies(response, loginToken);
        return "ok";
    }

    /**
     * 微信登录2-关联手机号
     *
     * @param token
     * @param encryptedData
     * @param iv
     * @param inviterCode
     * @param response
     * @return
     */
    @PostMapping("/v1.0/bindPhoneWithWechat")
    public String bindPhoneWithWechat(@RequestParam @NotBlank String token,
        @RequestParam @NotBlank String encryptedData, @RequestParam @NotBlank String iv,
        @RequestParam(required = false) String inviterCode, HttpServletResponse response) {
        String loginToken = loginService.bindPhoneWithWechat(token, encryptedData, iv, inviterCode);
        setCookies(response, loginToken);
        return "ok";
    }

    /**
     * 密码登录,明文string->明文byte[]->sha256->base64
     *
     * @param username
     * @param password
     * @param response
     * @return
     */
    @PostMapping("/v1.0/loginByPassword")
    public String loginByPassword(@RequestParam @NotBlank String username, @RequestParam @NotBlank String password,
        HttpServletResponse response) {
        // 同一个账号一秒钟只能尝试一次
        String s = redisTemplate.opsForValue().get(RedisPrefixTypeConstant.USER_LOGGED_IN_TRY_COUNT + username);
        if (s != null) {
            throw new VeException("操作太频繁");
        }
        redisTemplate.opsForValue()
            .set(RedisPrefixTypeConstant.USER_LOGGED_IN_TRY_COUNT + username, "1", 1L, TimeUnit.SECONDS);

        String token = loginService.loginByPassword(username, password);
        setCookies(response, token);

        UserLoginRelationDTO userLoginRelationDTO =
            JSON.parseObject(redisTemplate.opsForValue().get(RedisPrefixTypeConstant.USER_LOGGED_IN_TOKEN + token),
                UserLoginRelationDTO.class);
        if (userLoginRelationDTO == null) {
            return "";
        }
        return "";
    }

    private void setCookies(HttpServletResponse response, String token) {
        UserLoginRelationDTO userLoginRelationDTO =
            JSON.parseObject(redisTemplate.opsForValue().get(RedisPrefixTypeConstant.USER_LOGGED_IN_TOKEN + token),
                UserLoginRelationDTO.class);
        Long expire =
            redisTemplate.getExpire(RedisPrefixTypeConstant.USER_LOGGED_IN_TOKEN + token, TimeUnit.MILLISECONDS);
        int seconds = (int)(expire / 1000);
        //        addCookie(response, SessionCookie.Fields.expireAt, expire, seconds);
        //        addCookie(response, SessionCookie.Fields.accessToken, token, seconds);
        //        addCookie(response, SessionCookie.Fields.loginId, userLoginRelationDTO.getId(), seconds);
        //        addCookie(response, SessionCookie.Fields.userId, userLoginRelationDTO.getUserId(), seconds);
    }
}