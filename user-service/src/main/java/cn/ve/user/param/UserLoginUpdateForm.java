package cn.ve.user.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登陆表(UserLogin)实体类
 *
 * @author makejava
 * @since 2021-09-20 13:15:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserLoginUpdateForm extends UserLoginCreateForm {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

}
