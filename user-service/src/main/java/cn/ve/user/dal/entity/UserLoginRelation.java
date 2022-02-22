package cn.ve.user.dal.entity;

import cn.ve.base.pojo.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * 用户表(UserLoginRelation)实体类
 *
 * @author ve
 * @since 2022-02-22 14:16:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserLoginRelation extends BaseEntity {
    private static final long serialVersionUID = -80291923820424731L;


    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 修改人名称
     */
    private String updaterName;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 登录类型:0->微信登陆;1->手机号登录;2->账号密码登录;
     */
    private Integer loginType;

    /**
     * 账号或手机号或第三方openid
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

}
