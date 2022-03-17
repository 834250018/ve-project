package cn.ve.message.param;

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
    /**
     * 主键id
     */
    private Long id;

    /**
     * 状态
     */
    private Integer status;
}
