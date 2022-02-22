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
public class SmsMqParam extends BaseMqParam {

    private static final long serialVersionUID = 0L;
    /**
     * 短信签名
     */
    private String signName;
    /**
     * 接收短信号码
     */
    private String phoneNumbers;
    /**
     * 短信模板编码
     */
    private String templateCode;
    /**
     * 短信模板参数
     */
    private Map<String, String> paramMap;

    public SmsMqParam(AmqpTemplate amqpTemplate) {
        super(amqpTemplate);
    }

    @Override
    protected String setExchange() {
        return MqConstant.ALL2MESSAGE_SMS_EXCHANGE;
    }

    @Override
    protected String setRoutingKey() {
        return MqConstant.ALL2MESSAGE_SMS_ROUTING_KEY;
    }
}
