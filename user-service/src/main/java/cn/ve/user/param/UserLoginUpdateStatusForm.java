package cn.ve.user.param;

import lombok.Data;

import java.io.Serializable;

/**
 * 登陆表(UserLogin)实体类
 *
 * @author makejava
 * @since 2021-09-20 13:15:25
 */
@Data
public class UserLoginUpdateStatusForm implements Serializable {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 状态: 0.未启用, 1.已启用
     */
    private Integer status;

}
