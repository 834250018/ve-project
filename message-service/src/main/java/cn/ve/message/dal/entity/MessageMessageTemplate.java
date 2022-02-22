package cn.ve.message.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 系统消息模板表(MessageMessageTemplate)实体类
 *
 * @author ve
 * @since 2022-02-22 10:52:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MessageMessageTemplate extends BaseEntity {
    private static final long serialVersionUID = -42803589645553027L;

    /**
     * 模板标题
     */
    private String templateTitle;

    /**
     * 模板内容
     */
    private String templateContent;

    /**
     * 是否启用: 0.已禁用, 1.已启用
     */
    private Integer status;

    /**
     * 跳转路径
     */
    private String routeUri;

}
