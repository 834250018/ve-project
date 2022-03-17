package cn.ve.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ve
 * @date 2021/8/10
 */
@Data
public class LoginByWechatDTO implements Serializable {

    /**
     * 是否登录成功
     */
    private Boolean loggedIn;

    /**
     * 登录的token
     */
    private String loginToken;

    /**
     * 登陆失败的临时token,用于下一步关联手机号注册
     */
    private String tempToken;

}
