package cn.ve.user.dal.entity;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 登陆表(UserLogin)实体类
 *
 * @author makejava
 * @since 2021-09-20 13:15:12
 */
@Data
public class UserLogin implements Serializable {
    private static final long serialVersionUID = -12885040130305002L;
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
