package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ve
 * @date 2022/1/26
 */
@Data
public class UriParam implements Serializable {

    private String signature;
    private String timestamp;
    private String nonce;
    private Long echostr; // 响应字符串
}
