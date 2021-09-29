package cn.ve.user.config;

import cn.ve.base.constant.MqConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Configuration;

/**
 * 普通消息队列demo
 *
 * @author ve
 * @date 2021/7/29
 */
@Configuration
public class RabbitInitConfig {

    public RabbitInitConfig(AmqpAdmin amqpAdmin) {
        // 项目创建队列
        systemAlert(amqpAdmin);
    }

    private void systemAlert(AmqpAdmin amqpAdmin) {
        Queue queue = new Queue(MqConstant.ALL2MESSAGE_SYSTEM_ALERT_QUEUE, true);
        amqpAdmin.declareQueue(queue);
        DirectExchange userEvaluationScoreDirectExchange =
                new DirectExchange(MqConstant.ALL2MESSAGE_SYSTEM_ALERT_EXCHANGE, true, false);
        amqpAdmin.declareExchange(userEvaluationScoreDirectExchange);
        Binding binding = BindingBuilder.bind(queue).to(userEvaluationScoreDirectExchange)
                .with(MqConstant.ALL2MESSAGE_SYSTEM_ALERT_ROUTING_KEY);
        amqpAdmin.declareBinding(binding);
    }

}
