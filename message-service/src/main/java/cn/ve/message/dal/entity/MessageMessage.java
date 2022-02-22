package cn.ve.message.dal.entity;

import cn.ve.base.pojo.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 系统消息通知表(MessageMessage)实体类
 *
 * @author ve
 * @since 2022-02-22 10:52:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MessageMessage extends BaseEntity {
    private static final long serialVersionUID = 510218861863170653L;

    /**
     * 接收通知的用户id
     */
    private Long userId;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 是否已读: 0.未读, 1.已读
     */
    private Integer status;

    /**
     * 详情id,跳转后的页面详情id
     */
    private Long detailId;

    /**
     * 跳转路径
     */
    private String url;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 参数
     */
    private String param;

}
