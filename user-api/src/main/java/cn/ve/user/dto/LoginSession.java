package cn.ve.user.dto;

import lombok.Data;

@Data
public class LoginSession {
    /**
     * 用户表id
     */
    private String userId;
    /**
     * 登录表id
     */
    private String loginId;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 头像
     */
    private String headPortrait;
    /**
     * 昵称
     */
    private String nickname;
}