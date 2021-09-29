package cn.ve.user.param;

import cn.ve.base.constant.MqConstant;
import cn.ve.base.pojo.BaseMqParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.AmqpTemplate;

/**
 * @author ve
 * @date 2021/8/18
 */
@Getter
@Setter
public class SystemAlertMqParam extends BaseMqParam {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户类型:0.企业端,1.工人端,2.后台管理员
     */
    private Integer userType;
    /**
     * 姓名
     */
    private String name;
    /**
     * 头像
     */
    private String headPortrait;
    /**
     * 手机号
     */
    private String phone;

    @Override
    protected String setExchange() {
        return MqConstant.ALL2MESSAGE_SYSTEM_ALERT_EXCHANGE;
    }

    @Override
    protected String setRoutingKey() {
        return MqConstant.ALL2MESSAGE_SYSTEM_ALERT_ROUTING_KEY;
    }

    public SystemAlertMqParam(AmqpTemplate amqpTemplate) {
        super(amqpTemplate);
    }
}
