package cn.ve.user.dto;

import lombok.Data;

@Data
public class LoginSession {
    /**
     * 用户表id
     */
    private Long userId;
    /**
     * 登录表id
     */
    private Long loginId;
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