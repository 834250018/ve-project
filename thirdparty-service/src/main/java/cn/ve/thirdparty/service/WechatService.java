package cn.ve.thirdparty.service;

import cn.ve.thirdparty.pojo.OfficialAccountMsgParam;
import cn.ve.thirdparty.pojo.UriParam;

/**
 * @author ve
 * @date 2022/2/23
 */
public interface WechatService {

    /**
     * 此处发送的是公众号消息,appid跟secret都是公众号的,还需要配置公众号ip白名单
     *
     * @param param
     */
    void officialAccountMsg(OfficialAccountMsgParam param);

    String getAccessToken(String appid, String secret);

    /**
     * 加载已关注的用户的unionId
     */
    void loadUnionId();

    /**
     * 微信公众号关注回调
     * @param uriParam
     * @param xmlMsg
     * @return
     */
    Long wechatCallback(UriParam uriParam, String xmlMsg);
}
