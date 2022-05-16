package cn.ve.message.config;

import cn.ve.base.constant.MqConstant;
import cn.ve.base.util.StringConstant;
import cn.ve.message.api.param.MessageMqParam;
import cn.ve.message.api.param.SmsMqParam;
import cn.ve.message.dal.entity.MessageMessage;
import cn.ve.message.dal.entity.MessageMessageTemplate;
import cn.ve.message.dal.mapper.MessageMessageMapper;
import cn.ve.message.dal.mapper.MessageMessageTemplateMapper;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;

/**
 * 普通消息队列demo
 *
 * @author ve
 * @date 2021/7/29
 */
@Slf4j
//@Configuration
@DependsOn({"messageMessageMapper", "messageMessageTemplateMapper"})
public class RabbitMQListener {

    @Resource
    MessageMessageMapper messageMessageMapper;
    @Resource
    MessageMessageTemplateMapper messageMessageTemplateMapper;
    //        @Resource
    //        BaseSMSEngine baseSMSEngine;
    @Resource
    private RabbitMQConstant rabbitMQConstant;

    // 消费者使用以下代码
    @RabbitListener(queues = MqConstant.ALL2MESSAGE_MESSAGE_QUEUE)
    public void messageRabbitListener(Message message) {
        try {
            MessageMqParam messageMqParam = (MessageMqParam) SerializationUtils.deserialize(message.getBody());
            log.info(JSON.toJSONString(messageMqParam));
            MessageMessage messageMessage = new MessageMessage();
            messageMessage.setUserId(messageMqParam.getUserId());
            messageMessage.setStatus(0);
            messageMessage.setDetailId(messageMqParam.getDetailId());
            messageMessage.setTemplateId(messageMqParam.getTemplateId());
            MessageMessageTemplate messageMessageTemplate =
                    messageMessageTemplateMapper.selectById(messageMqParam.getTemplateId());
            if (messageMessageTemplate == null || messageMessageTemplate.getStatus() == 0) {
                // 模板不存在或模板未启用,直接丢弃消息
                return;
            }
            messageMessage.setUrl(messageMessageTemplate.getRouteUri());
            messageMessage.setTitle(messageMessageTemplate.getTemplateTitle());
            messageMessage.setContent(messageMessageTemplate.getTemplateContent());
            if (messageMqParam.getParamMap() != null) {
                messageMessage.setParam(JSON.toJSONString(messageMqParam.getParamMap()));
                messageMqParam.getParamMap().forEach((paramName, paramValue) -> {
                    if (StringUtils.isBlank(paramValue)) {
                        log.error("messageRabbitListener_参数{}的值为null", paramName);
                        return;
                    }

                    messageMessage.setTitle(messageMessage.getTitle().replaceAll(StringConstant.POUND + paramName + StringConstant.POUND, paramValue));
                    messageMessage
                            .setContent(messageMessage.getContent().replaceAll(StringConstant.POUND + paramName + StringConstant.POUND, paramValue));
                });
            }
            messageMessageMapper.insert(messageMessage);
        } catch (Exception e) {
            log.error("系统通知服务异常: {}", e.getMessage(), e);
        }
    }

    // 消费者使用以下代码
    @RabbitListener(queues = MqConstant.ALL2MESSAGE_SMS_QUEUE)
    public void smsRabbitListener(Message message) {
        SmsMqParam smsMqParam = (SmsMqParam) SerializationUtils.deserialize(message.getBody());
        log.info(JSON.toJSONString(smsMqParam));
        if (!rabbitMQConstant.getSmsSwitch()) {
            return;
        }
        // todo 发短信
        // todo 持久化到本地数据库
    }
}
