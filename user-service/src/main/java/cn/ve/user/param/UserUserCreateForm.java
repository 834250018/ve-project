package cn.ve.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表(UserUser)实体类
 *
 * @author ve
 * @since 2022-02-22 14:15:27
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
     * 手机号码
     */
    @ApiModelProperty("手机号码")
    private String phone;
    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String headPortrait;
    /**
     * 性别:0.男；1.女；2.未知；
     */
    @ApiModelProperty("性别:0.男；1.女；2.未知；")
    private Integer gender;
    /**
     * 现住地省编码
     */
    @ApiModelProperty("现住地省编码")
    private String provinceCode;
    /**
     * 现住地市编码
     */
    @ApiModelProperty("现住地市编码")
    private String cityCode;
    /**
     * 现住地区编码
     */
    @ApiModelProperty("现住地区编码")
    private String areaCode;
    /**
     * 现住地省名称
     */
    @ApiModelProperty("现住地省名称")
    private String provinceName;
    /**
     * 现住地市名称
     */
    @ApiModelProperty("现住地市名称")
    private String cityName;
    /**
     * 现住地区名称
     */
    @ApiModelProperty("现住地区名称")
    private String areaName;
    /**
     * 现住地详细地址
     */
    @ApiModelProperty("现住地详细地址")
    private String address;
    /**
     * 生日
     */
    @ApiModelProperty("生日")
    private Date birthday;
    /**
     * 职业
     */
    @ApiModelProperty("职业")
    private String occupation;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;
    /**
     * 民族
     */
    @ApiModelProperty("民族")
    private String nation;
    /**
     * 家乡
     */
    @ApiModelProperty("家乡")
    private String hometown;
    /**
     * 年龄
     */
    @ApiModelProperty("年龄")
    private Integer age;

}
