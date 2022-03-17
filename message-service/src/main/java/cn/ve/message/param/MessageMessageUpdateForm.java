package cn.ve.message.param;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统消息通知表(MessageMessage)实体类
 *
 * @author ve
 * @since 2022-02-22 10:12:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageMessageUpdateForm extends MessageMessageCreateForm {
    /**
     * 主键id
     */
    private Long id;
}
