package cn.ve.user.param;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户表(UserUser)实体类
 *
 * @author makejava
 * @since 2021-09-20 21:44:15
 */
@Data
public class UserUserUpdateStatusForm implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;
    /**
     * 状态: 0.未启用, 1.已启用
     */
    @ApiModelProperty("状态")
    private Integer status;

}
