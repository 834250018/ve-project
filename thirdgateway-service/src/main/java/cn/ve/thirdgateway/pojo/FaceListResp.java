package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FaceListResp implements Serializable {
    private String face_token;
}