package cn.ve.user.param;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 用户表(UserLoginRelation)实体类
 *
 * @author ve
 * @since 2022-02-22 14:16:51
 */
@Data
public class UserLoginRelationUpdateStatusForm implements Serializable {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("状态")
    private Integer status;
}
