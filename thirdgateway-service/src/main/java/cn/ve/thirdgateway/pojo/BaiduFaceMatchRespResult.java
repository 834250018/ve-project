package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
    public  class BaiduFaceMatchRespResult implements Serializable {
        /**
         * 分数,建议阈值为80
         */
        private Integer score;
        /**
         * face_token,如果后续要用到,需要使用
         */
        private List<FaceListResp> face_list;
    }