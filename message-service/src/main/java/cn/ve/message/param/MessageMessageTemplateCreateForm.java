package cn.ve.message.param;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统消息模板表(MessageMessageTemplate)实体类
 *
 * @author ve
 * @since 2022-02-22 10:25:19
 */
@Data
public class MessageMessageTemplateCreateForm implements Serializable {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 创建人id
     */
    private String creatorId;
    /**
     * 修改人id
     */
    private String updaterId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 创建人名称
     */
    private String creatorName;
    /**
     * 修改人名称
     */
    private String updaterName;
    /**
     * 是否被删除:0.未删除;1.已删除
     */
    private Integer deleted;
    /**
     * 版本号
     */
    private Integer versions;
    /**
     * 备注
     */
    private String remark;
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
