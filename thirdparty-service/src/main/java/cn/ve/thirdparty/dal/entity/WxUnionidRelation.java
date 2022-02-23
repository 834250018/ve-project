package cn.ve.thirdparty.dal.entity;

import cn.ve.base.pojo.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 微信公众号openid关联表(WxUnionidRelation)实体类
 *
 * @author ve
 * @since 2022-02-23 16:13:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class WxUnionidRelation extends BaseEntity {
    private static final long serialVersionUID = -64471244592350247L;

    /**
     * 用户公众号openid
     */
    private String openid;

    /**
     * 用户unionid
     */
    private String unionid;

    /**
     * 订阅状态: 0.取消订阅,1.订阅中
     */
    private Integer status;

}
