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
    String ALL2MESSAGE_MESSAGE_QUEUE = "all2message_message_queue";
    /**
     * 系统消息交换器
     */
    String ALL2MESSAGE_MESSAGE_EXCHANGE = "all2message_message_exchange";
    /**
     * 系统消息路由Key
     */
    String ALL2MESSAGE_MESSAGE_ROUTING_KEY = "all2message_message_routing_key";

    /**
     * mq消费失败消息队列
     */
    String ALL2COMMONS_ERR_MSG_EXCHANGE = "all2commons_err_msg_exchange";
    String ALL2COMMONS_ERR_MSG_QUEUE = "all2commons_err_msg_queue";
    String ALL2COMMONS_ERR_MSG_ROUTING_KEY = "all2commons_err_msg_routing_key";
    /**
     * mq停工自动复工延迟队列
     */
    String USER2USER_XXX_DELAY_EXCHANGE = "user2user_xxx_delay_exchange";
    String USER2USER_XXX_DELAY_QUEUE = "user2user_xxx_delay_queue";
    String USER2USER_XXX_DELAY_ROUTING_KEY = "user2user_xxx_delay_routing_key";

    /**
     * 用户个人信息变更广播
     */
    String USER2ALL_UPDATED_EXCHANGE = "user2all_updated_exchange";
    String USER2ALL_UPDATED_FANOUT_QUEUE_USER = "user2all_updated_fanout_queue_user";
    String USER2ALL_UPDATED_FANOUT_QUEUE_MARKETING = "user2all_updated_fanout_queue_marketing";
}
