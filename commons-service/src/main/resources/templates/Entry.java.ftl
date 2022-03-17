package ${group}.${projectName}.test;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* ${name?cap_first}Entry
* @author ve
* @date 2019/7/23 22:25
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ${name?cap_first}Entry {

private String id;

@ApiModelProperty(value = "姓名")
private String name;

@ApiModelProperty(value = "备注")
private String remark;

}