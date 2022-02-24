package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ve
 * @date 2021/8/2
 */
@Data
public class WechatOpenidDTO implements Serializable {

    /**
     * 用户唯一标识
     */
    String openid;
    /**
     * 会话密钥
     */
    String session_key;
    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 UnionID 机制说明。
     */
    String unionid;
    /**
     * 错误码
     * 文档地址https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
     */
    Integer errcode;
    /**
     * 错误信息
     */
    String errmsg;

}
