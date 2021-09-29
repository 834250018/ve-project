package cn.ve.user.param;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 用户表(UserUser)实体类
 *
 * @author makejava
 * @since 2021-09-20 21:44:15
 */
@Data
public class UserUserCreateForm implements Serializable {

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
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;

}
