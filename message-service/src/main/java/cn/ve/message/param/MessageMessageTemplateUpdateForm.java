package cn.ve.message.param;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统消息模板表(MessageMessageTemplate)实体类
 *
 * @author ve
 * @since 2022-02-22 10:25:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageMessageTemplateUpdateForm extends MessageMessageTemplateCreateForm {
    @ApiModelProperty("主键id")
    private Long id;
}
