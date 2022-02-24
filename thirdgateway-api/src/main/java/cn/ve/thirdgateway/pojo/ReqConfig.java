package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ve
 * @date 2021/9/7
 */

@Data
public   class ReqConfig implements Serializable {
    /**
     * 银行卡
     */
    private boolean card_type = true;
    /**
     * 身份证正反面类型
     * face:正面(人像面)
     * back反面(国徽面)
     */
    private String side;
}
