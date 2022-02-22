package cn.ve.base.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ve
 * @date 2018/7/25 15:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeErrorCode {
    private static final long serialVersionUID = 1L;
    private String status;
    private String msg;
}