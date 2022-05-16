package cn.ve.base.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VeResultCode {
    SUCCESS(200, "成功"),
    PARAMS_FORMAT_ERROR(400, "请求参数格式有误"),
    USER_NOT_LOGIN(401, "客户端未登录"),
    USER_NOT_EXISTS_OR_PWDERR(402, "用户不存在或密码错误"),
    UNAUTHORIZED_ACCESS(403, "无权访问"),
    ;
    private final int code;
    private final String msg;

}