package cn.ve.message.param;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 系统消息通知表(MessageMessage)实体类
 *
 * @author ve
 * @since 2022-02-22 10:12:31
 */
@Data
public class MessageMessageUpdateStatusForm implements Serializable {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("状态")
    private Integer status;
}
