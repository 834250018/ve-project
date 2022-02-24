package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ve
 * @date 2021/9/7
 */

@Data
public  class BankCardOcrResp implements Serializable {
    /**
     * 无则为""
     */
    private String bank_name;
    /**
     *
     */
    private String card_num;
    /**
     * 无则为"",多个以英文逗号隔开
     */
    private String valid_date;
    /**
     * DC(借记卡),  CC(贷记卡),  SCC(准贷记卡), DCC(存贷合一卡), PC(预付卡)
     */
    private String card_type;
    /**
     * 是否复印件
     */
    private String is_fake;
    /**
     *
     */
    private String request_id;
    /**
     *
     */
    private String success;
    /**
     * 文件存储路径
     */
    private String fileUri;
}