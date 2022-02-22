package cn.ve.message.param;

import cn.ve.rest.pojo.BaseQO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统消息通知表(MessageMessage)实体类
 *
 * @author ve
 * @since 2022-02-22 10:12:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageMessageQO extends BaseQO {

    /**
     * 用户id
     */
    private Long userId;

}
