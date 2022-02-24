package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaiduReq implements Serializable {
    /**
     * 图片信息
     */
    private String image;
    /**
     * BASE64 URL FACE_TOKEN
     */
    private String image_type;
    private String face_type;
    private String quality_control;
    private String liveness_control;
}