package cn.ve.user.param;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

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
    @ApiModelProperty("主键id")
    private Long id;
    /**
     * 创建人id
     */
    @ApiModelProperty("创建人id")
    private Long creatorId;
    /**
     * 修改人id
     */
    @ApiModelProperty("修改人id")
    private Long updaterId;
    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String creatorName;
    /**
     * 修改人名称
     */
    @ApiModelProperty("修改人名称")
    private String updaterName;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;
    /**
     * 是否被删除:0.未删除;1.已删除
     */
    @ApiModelProperty("是否被删除:0.未删除;1.已删除")
    private Integer deleted;
    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    private Integer versions;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;
    /**
     * 登录类型:0.微信登陆,1.手机号登录,2.账号密码登录
     */
    @ApiModelProperty("登录类型:0.微信登陆,1.手机号登录,2.账号密码登录")
    private Integer loginType;
    /**
     * 唯一标识,账号或openid
     */
    @ApiModelProperty("唯一标识,账号或openid")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;
    /**
     * 盐
     */
    @ApiModelProperty("盐")
    private String salt;

}
