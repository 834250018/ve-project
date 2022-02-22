package cn.ve.message.api.param;

import cn.ve.base.constant.MqConstant;
import cn.ve.rabbitmq.pojo.BaseMqParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.AmqpTemplate;

import java.util.Map;

/**
 * 消息队列-短信消息
 *
 * @author ve
 * @date 2021/8/16
 */
@Getter
@Setter
public class MessageMqParam extends BaseMqParam {

    private static final long serialVersionUID = 0L;

    /**
     * 系统通知模板id
     */
    private Long templateId;
    /**
     * 系统通知模板参数
     */
    private Map<String, String> paramMap;
    /**
     * 跳转后的详情id,部分模板需要
     */
    private Long detailId;
    /**
     * 接收的用户id
     */
    private Long userId;

    public MessageMqParam(AmqpTemplate amqpTemplate) {
        super(amqpTemplate);
    }

    @Override
    protected String setExchange() {
        return MqConstant.ALL2MESSAGE_MESSAGE_EXCHANGE;
    }

    @Override
    protected String setRoutingKey() {
        return MqConstant.ALL2MESSAGE_MESSAGE_ROUTING_KEY;
    }
}
