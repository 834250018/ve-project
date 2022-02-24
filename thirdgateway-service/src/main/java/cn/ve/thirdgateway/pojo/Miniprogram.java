package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Miniprogram implements Serializable {
    /**
     * 必须,小程序appid
     */
    private String appid;
    /**
     * 非必须,小程序路径
     */
    private String pagepath;
}