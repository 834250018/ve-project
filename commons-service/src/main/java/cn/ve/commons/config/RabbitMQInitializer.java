package cn.ve.commons.config;

import cn.ve.base.constant.MqConstant;
import org.springframework.amqp.core.*;

import java.util.HashMap;

/**
 * 普通消息队列demo
 *
 * @author ve
 * @date 2021/7/29
 */
//@Configuration
public class RabbitMQInitializer {

    private static final String X_DELAYED_TYPE = "x-delayed-type";
    private static final String DIRECT = "direct";
    private static final String X_DELAYED_MESSAGE = "x-delayed-message";

    public RabbitMQInitializer(AmqpAdmin amqpAdmin) {
        // 短信队列
        sms(amqpAdmin);

        // 系统通知队列
        message(amqpAdmin);

        // 更新用户信息广播
        userUpdatedFanout(amqpAdmin);

        // mq消费失败消息队列
        errMsg(amqpAdmin);

        // mq停工自动复工延迟队列
        returnToWorkMsg(amqpAdmin);

    }

    private void returnToWorkMsg(AmqpAdmin amqpAdmin) {
        delayQueueDurable(amqpAdmin, MqConstant.USER2USER_XXX_DELAY_QUEUE, MqConstant.USER2USER_XXX_DELAY_EXCHANGE,
            MqConstant.USER2USER_XXX_DELAY_ROUTING_KEY);
    }

    private void message(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.ALL2MESSAGE_MESSAGE_QUEUE, MqConstant.ALL2MESSAGE_MESSAGE_EXCHANGE,
            MqConstant.ALL2MESSAGE_MESSAGE_ROUTING_KEY);
    }

    private void sms(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.ALL2MESSAGE_SMS_QUEUE, MqConstant.ALL2MESSAGE_SMS_EXCHANGE,
            MqConstant.ALL2MESSAGE_SMS_ROUTING_KEY);
    }

    private void userUpdatedFanout(AmqpAdmin amqpAdmin) {
        FanoutExchange userUpdatedFanoutExchange =
            new FanoutExchange(MqConstant.USER2ALL_UPDATED_EXCHANGE, Boolean.TRUE, Boolean.FALSE);
        amqpAdmin.declareExchange(userUpdatedFanoutExchange);
        Queue queue1 = new Queue(MqConstant.USER2ALL_UPDATED_FANOUT_QUEUE_USER, Boolean.TRUE);
        amqpAdmin.declareQueue(queue1);
        Queue queue3 = new Queue(MqConstant.USER2ALL_UPDATED_FANOUT_QUEUE_MARKETING, Boolean.TRUE);
        amqpAdmin.declareQueue(queue3);
        Binding binding1 = BindingBuilder.bind(queue1).to(userUpdatedFanoutExchange);
        amqpAdmin.declareBinding(binding1);
        Binding binding3 = BindingBuilder.bind(queue3).to(userUpdatedFanoutExchange);
        amqpAdmin.declareBinding(binding3);
    }

    private void errMsg(AmqpAdmin amqpAdmin) {
        String queueName = MqConstant.ALL2COMMONS_ERR_MSG_QUEUE;
        String exchangeName = MqConstant.ALL2COMMONS_ERR_MSG_EXCHANGE;
        String routingKey = MqConstant.ALL2COMMONS_ERR_MSG_ROUTING_KEY;
        directQueueDurable(amqpAdmin, queueName, exchangeName, routingKey);
    }

    /**
     * 持久化一套Direct队列
     *
     * @param amqpAdmin    注入
     * @param queueName    队列名
     * @param exchangeName 交换机名
     * @param routingKey   配对键
     */
    private void directQueueDurable(AmqpAdmin amqpAdmin, String queueName, String exchangeName, String routingKey) {
        Queue queue = new Queue(queueName, Boolean.TRUE);
        amqpAdmin.declareQueue(queue);
        DirectExchange smsDirectExchange = new DirectExchange(exchangeName, Boolean.TRUE, Boolean.FALSE);
        amqpAdmin.declareExchange(smsDirectExchange);
        Binding binding = BindingBuilder.bind(queue).to(smsDirectExchange).with(routingKey);
        amqpAdmin.declareBinding(binding);
    }

    /**
     * 持久化一个延迟队列
     *
     * @param amqpAdmin    注入
     * @param queueName    队列名
     * @param exchangeName 交换机名
     * @param routingKey   配对键
     */
    private void delayQueueDurable(AmqpAdmin amqpAdmin, String queueName, String exchangeName, String routingKey) {
        Queue queue = new Queue(queueName, Boolean.TRUE);
        amqpAdmin.declareQueue(queue);
        CustomExchange exchange = new CustomExchange(exchangeName, X_DELAYED_MESSAGE, Boolean.TRUE, Boolean.FALSE, getDelayQueueArgs());
        amqpAdmin.declareExchange(exchange);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
        amqpAdmin.declareBinding(binding);
    }

    /**
     * 延迟队列必要的头参数
     *
     * @return
     */
    private HashMap<String, Object> getDelayQueueArgs() {
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put(X_DELAYED_TYPE, DIRECT);
        return arguments;
    }

}
