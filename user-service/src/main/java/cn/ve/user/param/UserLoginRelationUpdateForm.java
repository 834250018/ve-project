package cn.ve.user.param;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户表(UserLoginRelation)实体类
 *
 * @author ve
 * @since 2022-02-22 14:16:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserLoginRelationUpdateForm extends UserLoginRelationCreateForm {
    @ApiModelProperty("主键id")
    private Long id;
}
