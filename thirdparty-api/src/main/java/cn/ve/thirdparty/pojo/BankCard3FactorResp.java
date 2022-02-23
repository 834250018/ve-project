package cn.ve.thirdparty.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ve
 * @date 2021/9/7
 */

@Data
public class BankCard3FactorResp implements Serializable {
    /**
     * 错误码	错误信息	描述
     * 400	参数错误	参数错误
     * 404	请求资源不存在	请求资源不存在
     * 500	系统内部错误，请联系服务商	系统内部错误，请联系服务商
     * 501	第三方服务异常	第三方服务异常
     * 604	接口停用	接口停用
     * 1001	其他，以实际返回为准	其他，以实际返回为准
     */
    private String msg;
    /**
     * true
     */
    private Boolean success;
    /**
     * 200
     */
    private Integer code;
    private BankCard3FactorRespData data;
}