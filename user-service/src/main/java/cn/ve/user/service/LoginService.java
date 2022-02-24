package cn.ve.user.service;

import cn.ve.thirdgateway.pojo.WechatOpenidDTO;
import cn.ve.user.dto.LoginByWechatDTO;

/**
 * @author ve
 * @date 2021/8/2
 */
public interface LoginService {
    String STR = "something";

    // 手机号码注册并登录验证码
    void sendLoginCodeMsg(String phone);

    // 手机号码注册并登录
    String loginByPhone(String phone, String code, String inviterCode);

    // 微信登录并注册
    LoginByWechatDTO loginByWechat(String jscode, String headPortrait, String realName);

    // 微信仅登录不注册(静默授权)
    String loginOnlyByWechat(String jscode);

    // 获取微信openId
    WechatOpenidDTO getOpenidByJscode(String jscode);

    String bindPhoneWithWechat(String uuid, String encryptedData, String iv, String inviterCode);

    void removeToken();

    // 账号密码登录
    String loginByPassword(String username, String password);
}
