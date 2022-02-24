package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
    public  class BaiduFaceMatchResp implements Serializable {
        private Integer error_code;
        private String error_msg;
        private Integer timestamp;
        private Long log_id;
        private Integer cached;
        private BaiduFaceMatchRespResult result;
    }