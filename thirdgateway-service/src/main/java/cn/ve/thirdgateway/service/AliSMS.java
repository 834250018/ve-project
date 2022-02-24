package cn.ve.thirdgateway.service;

import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author ve
 * @date 2021/8/16
 */
@Slf4j
public class AliSMS extends BaseSMSEngine {

    private Client aliClient;

    @SneakyThrows
    public AliSMS(String accessKeyId, String accessKeySecret) {
        Config config = new Config().setAccessKeyId(accessKeyId).setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        aliClient = new Client(config);
    }

    @Override
    public void sendMsg(String signName, String phoneNumbers, String templateCode, Map<String, String> paramMap) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(phoneNumbers);
        sendSmsRequest.setSignName(signName);
        sendSmsRequest.setTemplateCode(templateCode);
        sendSmsRequest.setTemplateParam(JSONUtil.toJsonStr(paramMap));
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = aliClient.sendSms(sendSmsRequest);
            log.info(JSONUtil.toJsonStr(sendSmsResponse));
        } catch (Exception e) {
            log.error("阿里云短信服务异常: {}", e.getMessage(), e);
        }
        if (sendSmsResponse != null && !"OK".equals(sendSmsResponse.getBody().getCode())) {
            log.error("阿里云短信服务异常: message: {}, bizId: {}, requestId: {}", sendSmsResponse.getBody().getMessage(),
                sendSmsResponse.getBody().getBizId(), sendSmsResponse.getBody().getRequestId());
        }
    }

}
