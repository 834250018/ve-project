package cn.ve.user.param;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 登陆表(UserLogin)实体类
 *
 * @author makejava
 * @since 2021-09-20 13:15:25
 */
@Data
public class UserLoginCreateForm implements Serializable {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 创建人id
     */
    private Long creatorId;
    /**
     * 修改人id
     */
    private Long updaterId;
    /**
     * 创建人名称
     */
    private String creatorName;
    /**
     * 修改人名称
     */
    private String updaterName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 是否被删除:0.未删除;1.已删除
     */
    private Integer deleted;
    /**
     * 版本号
     */
    private Integer versions;
    /**
     * 备注
     */
    private String remark;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 登录类型:0.微信登陆,1.手机号登录,2.账号密码登录
     */
    private Integer loginType;
    /**
     * 唯一标识,账号或openid
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
