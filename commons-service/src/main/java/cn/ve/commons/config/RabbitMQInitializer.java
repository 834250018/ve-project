package cn.ve.commons.config;

import cn.ve.base.constant.MqConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Configuration;

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
/*
        // 更新用户信息广播
        userUpdatedFanout(amqpAdmin);

        // 用户行为队列
        userLogBehavioral(amqpAdmin);

        // 用户评价分更新队列
        userEvaluationScore(amqpAdmin);

        // 项目创建队列
        createProject(amqpAdmin);

        // 服务单统计队列
        orderCount(amqpAdmin);

        // 邀请用户注册通知消息
        userRegister(amqpAdmin);

        // 工人退场
        workerExit(amqpAdmin);

        // 预结算
        workerPreBalance(amqpAdmin);

        // 邀请有礼
        inviteGift(amqpAdmin);

        // 工作分享
        workShare(amqpAdmin);

        // 工人认证
        userAuth(amqpAdmin);

        // 项目产值
        outputValue(amqpAdmin);

        //支付结果
        pay(amqpAdmin);
        // 工人待结算金额增量
        unsettleAmtInc(amqpAdmin);
        // 工人待结算金额增量
        updateBizCardStatus(amqpAdmin);

        // 工作天数增量队列
        workDaysInc(amqpAdmin);

        // mq消费失败消息队列
        errMsg(amqpAdmin);

        // mq停工自动复工延迟队列
        returnToWorkMsg(amqpAdmin);*/
    }

    private void message(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.ALL2MESSAGE_MESSAGE_QUEUE, MqConstant.ALL2MESSAGE_MESSAGE_EXCHANGE,
            MqConstant.ALL2MESSAGE_MESSAGE_ROUTING_KEY);
    }

    private void sms(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.ALL2MESSAGE_SMS_QUEUE, MqConstant.ALL2MESSAGE_SMS_EXCHANGE,
            MqConstant.ALL2MESSAGE_SMS_ROUTING_KEY);
    }
/*
    private void userRegister(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.USER2MARKETING_REGISTER_QUEUE,
            MqConstant.USER2MARKETING_REGISTER_EXCHANGE, MqConstant.USER2MARKETING_REGISTER_ROUTING_KEY);
    }

    private void workerExit(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.ALL2MARKETING_OUT_QUEUE, MqConstant.ALL2MARKETING_OUT_EXCHANGE,
            MqConstant.ALL2MARKETING_OUT_ROUTING_KEY);
    }

    private void workerPreBalance(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.PROJECT2MARKETING_BALANCE_QUEUE,
            MqConstant.PROJECT2MARKETING_BALANCE_EXCHANGE, MqConstant.PROJECT2MARKETING_BALANCE_ROUTING_KEY);
    }

    private void inviteGift(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.PROJECT2MARKETING_ATTENDANCE_QUEUE,
            MqConstant.PROJECT2MARKETING_ATTENDANCE_EXCHANGE, MqConstant.PROJECT2MARKETING_ATTENDANCE_ROUTING_KEY);
    }

    private void workShare(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.BILL2MARKETING_ENROLL_QUEUE, MqConstant.BILL2MARKETING_ENROLL_EXCHANGE,
            MqConstant.BILL2MARKETING_ENROLL_ROUTING_KEY);
    }

    private void userAuth(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.USER2MARKETING_AUTH_QUEUE, MqConstant.USER2MARKETING_AUTH_EXCHANGE,
            MqConstant.USER2MARKETING_AUTH_ROUTING_KEY);
    }

    private void orderCount(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.BILL2USER_ORDER_COUNT_QUEUE, MqConstant.BILL2USER_ORDER_COUNT_EXCHANGE,
            MqConstant.BILL2USER_ORDER_COUNT_ROUTING_KEY);
    }

    private void createProject(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.BILL2PROJECT_CREATE_PROJECT_QUEUE,
            MqConstant.BILL2PROJECT_CREATE_PROJECT_EXCHANGE, MqConstant.BILL2PROJECT_CREATE_PROJECT_ROUTING_KEY);
    }

    private void userEvaluationScore(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.BILL2USER_EVALUATION_SCORE_QUEUE,
            MqConstant.BILL2USER_EVALUATION_SCORE_EXCHANGE, MqConstant.BILL2USER_EVALUATION_SCORE_ROUTING_KEY);
    }

    private void userLogBehavioral(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.ALL2USER_LOG_BEHAVIORAL_QUEUE,
            MqConstant.ALL2USER_LOG_BEHAVIORAL_EXCHANGE, MqConstant.ALL2USER_LOG_BEHAVIORAL_ROUTING_KEY);
    }

    private void userUpdatedFanout(AmqpAdmin amqpAdmin) {
        FanoutExchange userUpdatedFanoutExchange =
            new FanoutExchange(MqConstant.USER2ALL_UPDATED_EXCHANGE, true, false);
        amqpAdmin.declareExchange(userUpdatedFanoutExchange);
        Queue queue1 = new Queue(MqConstant.USER2ALL_UPDATED_FANOUT_QUEUE_USER, true);
        amqpAdmin.declareQueue(queue1);
        Queue queue2 = new Queue(MqConstant.USER2ALL_UPDATED_FANOUT_QUEUE_BILL, true);
        amqpAdmin.declareQueue(queue2);
        Queue queue3 = new Queue(MqConstant.USER2ALL_UPDATED_FANOUT_QUEUE_MARKETING, true);
        amqpAdmin.declareQueue(queue3);
        Queue queue4 = new Queue(MqConstant.USER2ALL_UPDATED_FANOUT_QUEUE_BANG_PROJECT, true);
        amqpAdmin.declareQueue(queue4);
        Binding binding1 = BindingBuilder.bind(queue1).to(userUpdatedFanoutExchange);
        amqpAdmin.declareBinding(binding1);
        Binding binding2 = BindingBuilder.bind(queue2).to(userUpdatedFanoutExchange);
        amqpAdmin.declareBinding(binding2);
        Binding binding3 = BindingBuilder.bind(queue3).to(userUpdatedFanoutExchange);
        amqpAdmin.declareBinding(binding3);
        Binding binding4 = BindingBuilder.bind(queue4).to(userUpdatedFanoutExchange);
        amqpAdmin.declareBinding(binding4);
    }

    private void outputValue(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.PROJECT2BILL_OUTPUT_VALUE_QUEUE,
            MqConstant.PROJECT2BILL_OUTPUT_VALUE_EXCHANGE, MqConstant.PROJECT2BILL_OUTPUT_VALUE_ROUTING_KEY);
    }

    private void pay(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.ALL2BILL_PAY_RESULT_QUEUE, MqConstant.ALL2BILL_PAY_RESULT_EXCHANGE,
            MqConstant.ALL2BILL_PAY_RESULT_ROUTING_KEY);
    }

    private void unsettleAmtInc(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.PROJECT2USER_UNSETTLE_AMT_INC_QUEUE,
            MqConstant.PROJECT2USER_UNSETTLE_AMT_INC_EXCHANGE, MqConstant.PROJECT2USER_UNSETTLE_AMT_INC_ROUTING_KEY);
    }

    private void updateBizCardStatus(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.PROJECT2USER_UPDATE_BIZ_CARD_STATUS_QUEUE,
            MqConstant.PROJECT2USER_UPDATE_BIZ_CARD_STATUS_EXCHANGE,
            MqConstant.PROJECT2USER_UPDATE_BIZ_CARD_STATUS_ROUTING_KEY);
    }

    private void workDaysInc(AmqpAdmin amqpAdmin) {
        directQueueDurable(amqpAdmin, MqConstant.PROJECT2USER_WORK_DAYS_INC_QUEUE,
            MqConstant.PROJECT2USER_WORK_DAYS_INC_EXCHANGE, MqConstant.PROJECT2USER_WORK_DAYS_INC_ROUTING_KEY);
    }

    private void errMsg(AmqpAdmin amqpAdmin) {
        String queueName = MqConstant.ALL2COMMONS_ERR_MSG_QUEUE;
        String exchangeName = MqConstant.ALL2COMMONS_ERR_MSG_EXCHANGE;
        String routingKey = MqConstant.ALL2COMMONS_ERR_MSG_ROUTING_KEY;
        directQueueDurable(amqpAdmin, queueName, exchangeName, routingKey);
    }

    private void returnToWorkMsg(AmqpAdmin amqpAdmin) {
        delayQueueDurable(amqpAdmin, MqConstant.BANGBANGPROJECT2BANGBANGPROJECT_TURN_TO_WORK_DELAY_QUEUE,
            MqConstant.BANGBANGPROJECT2BANGBANGPROJECT_TURN_TO_WORK_DELAY_EXCHANGE,
            MqConstant.BANGBANGPROJECT2BANGBANGPROJECT_TURN_TO_WORK_DELAY_ROUTING_KEY);
    }*/

    /**
     * 持久化一套Direct队列
     *
     * @param amqpAdmin    注入
     * @param queueName    队列名
     * @param exchangeName 交换机名
     * @param routingKey   配对键
     */
    private void directQueueDurable(AmqpAdmin amqpAdmin, String queueName, String exchangeName, String routingKey) {
        Queue queue = new Queue(queueName, true);
        amqpAdmin.declareQueue(queue);
        DirectExchange smsDirectExchange = new DirectExchange(exchangeName, true, false);
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
        Queue queue = new Queue(queueName, true);
        amqpAdmin.declareQueue(queue);
        CustomExchange exchange = new CustomExchange(exchangeName, X_DELAYED_MESSAGE, true, false, getDelayQueueArgs());
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
