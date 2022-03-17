package cn.ve.thirdgateway.service;

import java.util.Map;

/**
 * @author ve
 * @date 2021/8/16
 */
public abstract class BaseSMSEngine {

    public abstract void sendMsg(String signName, String phoneNumbers, String templateCode,
        Map<String, String> paramMap);

}
