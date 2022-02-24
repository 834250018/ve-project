package cn.ve.thirdgateway.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ve
 * @date 2022/1/26
 */
@Data
public class OfficialAccountMsgParam implements Serializable {

    private static final long serialVersionUID = 0L;

    private String unionid;
    private String title;
    private String name;
    private String time;
    private String status;
    private String remark;
}
