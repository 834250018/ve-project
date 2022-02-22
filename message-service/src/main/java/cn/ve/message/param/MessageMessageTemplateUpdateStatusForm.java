package cn.ve.message.param;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 系统消息模板表(MessageMessageTemplate)实体类
 *
 * @author ve
 * @since 2022-02-22 10:25:19
 */
@Data
public class MessageMessageTemplateUpdateStatusForm implements Serializable {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("状态")
    private Integer status;
}
