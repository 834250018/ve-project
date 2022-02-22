package cn.ve.message.dto;

import java.util.Date;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统消息模板表(MessageMessageTemplate)实体类
 *
 * @author ve
 * @since 2022-02-22 10:25:19
 */
@Data
public class MessageMessageTemplateDTO implements Serializable {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
        @ApiModelProperty("主键id")
    private Long id;
        @ApiModelProperty("创建人id")
    private String creatorId;
        @ApiModelProperty("修改人id")
    private String updaterId;
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
        @ApiModelProperty("模板标题")
    private String templateTitle;
        @ApiModelProperty("模板内容")
    private String templateContent;
        @ApiModelProperty("是否启用: 0.已禁用, 1.已启用")
    private Integer status;
        @ApiModelProperty("跳转路径")
    private String routeUri;

}
