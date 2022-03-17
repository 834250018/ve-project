package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaiduAuthResp implements Serializable {
    private String refresh_token;
    private String expires_in;
    private String session_key;
    private String access_token;
    private String scope;
    private String session_secret;
}