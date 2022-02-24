package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ve
 * @date 2021/9/7
 */
@Data
public class OcrReq implements Serializable {
    /**
     * 图片base64
     */
    private String image;
    private ReqConfig configure = new ReqConfig();
}