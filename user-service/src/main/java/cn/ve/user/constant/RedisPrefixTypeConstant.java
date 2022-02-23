package cn.ve.user.constant;

/**
 * 验证码类型
 *
 * @author ve
 * @date 2021/8/9
 */
public interface RedisPrefixTypeConstant {
    /**
     * 手机号登录验证码
     */
    String USER_PHONE_LOGIN = "user_phone_login:";
    /**
     * 设置密码验证码
     */
    String SET_PASSWORD_LOGIN = "user_set_password:";
    /**
     * 修改手机号-旧号码验证码
     */
    String USER_UPDATE_PHONE_OLD = "user_update_phone_old:";
    /**
     * 修改手机号-新号码验证码
     */
    String USER_UPDATE_PHONE_NEW = "user_update_phone_new:";
    /**
     * 绑定银行卡验证码
     */
    String USER_BIND_BANK_CARD = "user_bind_bank_card:";
    /**
     * 修改银行卡验证码
     */
    String USER_UPDATE_BANK_CARD = "user_update_bank_card:";
    /**
     * 微信登录绑定手机号验证码
     */
    String USER_WECHAT_BIND_PHONE = "user_wechat_bind_phone:";
    /**
     * 记录上一次token
     */
    String USER_LAST_USER_TOKEN = "user_last_user_token:";
    /**
     * 存储用户信息
     */
    String USER_LOGGED_IN_TOKEN = "user_logged_in_token:";
    /**
     * 存储用户信息
     */
    String USER_LOGGED_IN_TRY_COUNT = "user_logged_in_try_count:";
    /**
     * 存储用户信息
     */
    String USER_BANK_CARD_3_FACTOR = "user_bank_card_3_factor:";
    /**
     * 存储用户信息
     */
    String USER_UPDATE_WITHDRAWAL_PWD = "user_update_withdrawal_pwd:";
    /**
     * 校验提现密码,同一天内连续输错三次则锁定,输对一次则重置
     */
    String USER_CHECK_WITHDRAWAL_PWD = "user_check_withdrawal_pwd:";

}
