package cn.ve.base.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 登录类型:0.微信登陆,1.手机号登录,2.账号密码登录
 *
 * @author ve
 * @date 2021/8/9
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    WECHAT(0, "小程序登陆"), PHONE(1, "手机号登录"), PASSWORD(2, "账号密码登录");

    private final int code;
    private final String name;

    public static String getNameByValue(String code) {
        if (StringUtils.isBlank(code)) {
            return code;
        }
        for (LoginTypeEnum realNameAuthEnum : values()) {
            if (realNameAuthEnum.code == Integer.parseInt(code)) {
                return realNameAuthEnum.name;
            }
        }
        return null;
    }

}
