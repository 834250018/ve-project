package cn.ve.user.config;

import cn.ve.user.param.SystemAlertMqParam;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author ve
 * @date 2021/8/16
 */
@Slf4j
@Configuration
@DependsOn()
public class RabbitConfig {

    @RabbitListener(queues = "all2message_system_alert_queue")
    public void smsListener(Message message) {
        SystemAlertMqParam systemAlertMqParam = (SystemAlertMqParam)SerializationUtils.deserialize(message.getBody());
        log.info(JSON.toJSONString(systemAlertMqParam));
        System.out.println("mq ok");
    }

}
