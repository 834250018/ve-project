package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ve
 * @date 2021/9/7
 */
@Data
public class BankCard3FactorRespData implements Serializable {
    /**
     * 订单号
     */
    private String order_no;
    /**
     * 结果0 一致，1 不一致，2未认证，3 已注销
     */
    private Integer result;
    /**
     * 验证结果描述信息
     */
    private String desc;
    /**
     * 验证结果
     */
    private String msg;
}
