package cn.ve.user.service;

/**
 * @author ve
 * @date 2021/8/9
 */
public interface VerificationCodeService {

    /**
     * 发送短信验证码
     *
     * @param prefixType
     * @param phoneNo
     */
    void sendSMSCode(String prefixType, String phoneNo);

    /**
     * 校验验证码并删除缓存
     *
     * @param prefixType
     * @param appType
     * @param phoneNo
     * @param code
     */
    void checkSMSCodeAndRemove(String prefixType, String phoneNo, String code);

    /**
     * 校验验证码,不删除缓存
     *
     * @param prefixType
     * @param appType
     * @param phoneNo
     * @param code
     */
    void checkSMSCode(String prefixType, String phoneNo, String code);
}
