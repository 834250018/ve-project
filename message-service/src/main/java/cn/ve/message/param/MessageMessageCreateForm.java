package cn.ve.message.param;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 系统消息通知表(MessageMessage)实体类
 *
 * @author ve
 * @since 2022-02-22 10:12:31
 */
@Data
public class MessageMessageCreateForm implements Serializable {

        @ApiModelProperty("主键id")
    private Long id;
        @ApiModelProperty("创建人id")
    private Long creatorId;
        @ApiModelProperty("修改人id")
    private Long updaterId;
        @ApiModelProperty("创建时间")
    private Date createTime;
        @ApiModelProperty("修改时间")
    private Date updateTime;
        @ApiModelProperty("创建人名称")
    private String creatorName;
        @ApiModelProperty("修改人名称")
    private String updaterName;
        @ApiModelProperty("是否被删除:0.未删除;1.已删除")
    private Integer deleted;
        @ApiModelProperty("版本号")
    private Integer versions;
        @ApiModelProperty("备注")
    private String remark;
        @ApiModelProperty("接收通知的用户id")
    private Long userId;
        @ApiModelProperty("通知标题")
    private String title;
        @ApiModelProperty("通知内容")
    private String content;
        @ApiModelProperty("是否已读: 0.未读, 1.已读")
    private Integer status;
        @ApiModelProperty("详情id,跳转后的页面详情id")
    private Long detailId;
        @ApiModelProperty("跳转路径")
    private String url;
        @ApiModelProperty("模板id")
    private Long templateId;
        @ApiModelProperty("参数")
    private String param;

}
