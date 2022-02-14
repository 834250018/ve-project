package cn.ve.base.constant;

/**
 * 消息队列常量
 * 命名规则:
 * 生产者2消费者_业务_queue
 * 生产者2消费者_业务_exchange
 * 生产者2消费者_业务_routing_key
 * 如果生产者或消费者不明确,则使用all替代
 *
 * @author ve
 * @date 2021/8/16
 */
public interface MqConstant {
    /**
     * 短信队列
     */
    String ALL2MESSAGE_SMS_QUEUE = "all2message_sms_queue";
    /**
     * 短信交换器
     */
    String ALL2MESSAGE_SMS_EXCHANGE = "all2message_sms_exchange";
    /**
     * 短信路由Key
     */
    String ALL2MESSAGE_SMS_ROUTING_KEY = "all2message_sms_routing_key";

    /**
     * 系统消息队列
     */
    String ALL2MESSAGE_SYSTEM_ALERT_QUEUE = "all2message_system_alert_queue";
    /**
     * 系统消息交换器
     */
    String ALL2MESSAGE_SYSTEM_ALERT_EXCHANGE = "all2message_system_alert_exchange";
    /**
     * 系统消息路由Key
     */
    String ALL2MESSAGE_SYSTEM_ALERT_ROUTING_KEY = "all2message_system_alert_routing_key";

}
