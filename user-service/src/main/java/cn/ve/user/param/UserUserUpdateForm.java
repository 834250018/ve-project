package cn.ve.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户表(UserUser)实体类
 *
 * @author makejava
 * @since 2021-09-20 21:44:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserUserUpdateForm extends UserUserCreateForm {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

}
