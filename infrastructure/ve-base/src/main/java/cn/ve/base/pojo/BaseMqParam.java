package cn.ve.base.pojo;

import org.springframework.amqp.core.AmqpTemplate;

import java.io.Serializable;

/**
 * 消息队列-消息抽象父类
 *
 * @author ve
 * @date 2021/8/16
 */
public abstract class BaseMqParam implements Serializable {

    private final transient AmqpTemplate amqpTemplate;
    /**
     * 交换机
     */
    private final transient String exchange;
    /**
     * 配对key
     */
    private final transient String routingKey;

    public BaseMqParam(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
        this.exchange = setExchange();
        this.routingKey = setRoutingKey();
    }

    /**
     * 重写此方法确定交换器
     *
     * @return
     */
    protected abstract String setExchange();

    /**
     * 重写此方法确定路由key
     *
     * @return
     */
    protected abstract String setRoutingKey();

    public void sendMessage() {
        amqpTemplate.convertAndSend(exchange, routingKey, this);
    }

}
