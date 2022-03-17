package cn.ve.user.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户表(UserUser)实体类
 *
 * @author ve
 * @since 2022-02-22 14:15:28
 */
@Data
public class UserUserUpdateStatusForm implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 状态
     */
    private Integer status;
}
