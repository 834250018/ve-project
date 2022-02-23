package cn.ve.thirdparty.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class WxMessage implements Serializable {
    /**
     * 必须,openid
     */
    private String touser;
    /**
     * 必须,openid
     */
    private String template_id;
    /**
     * 可空
     */
    private String url;
    /**
     * 必须,openid
     */
    private String topcolor;
    /**
     * 小程序,非必须
     */
    private Miniprogram miniprogram;
    /**
     * 模板数据,必须
     */
    private Map<String, DataBody> data;
}
