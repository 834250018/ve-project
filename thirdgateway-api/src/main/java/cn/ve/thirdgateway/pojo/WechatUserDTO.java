package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ve
 * @date 2021/8/10
 */
@Data
public class WechatUserDTO implements Serializable {
    private String phoneNumber;
    private String purePhoneNumber;
    private String countryCode;
    private WatermarkDTO watermark;

    @Data
    public static class WatermarkDTO {
        private String timestamp;
        private String appid;

    }
}
