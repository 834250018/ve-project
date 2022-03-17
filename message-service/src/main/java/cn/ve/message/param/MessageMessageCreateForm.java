package cn.ve.message.param;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统消息通知表(MessageMessage)实体类
 *
 * @author ve
 * @since 2022-02-22 10:12:31
 */
@Data
public class MessageMessageCreateForm implements Serializable {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 创建人id
     */
    private Long creatorId;
    /**
     * 修改人id
     */
    private Long updaterId;
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
