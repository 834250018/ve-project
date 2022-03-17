package cn.ve.message.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * rabbit动态配置
 *
 * @author ve
 * @date 2021/7/29
 */
@Data
@RefreshScope
//@Configuration
public class RabbitMQConstant {

    @Value("${switch.sms:true}")
    private Boolean smsSwitch;
}
