package cn.ve.message.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ve
 * @date 2021/8/3
 */
@Data
public abstract class BaseQO implements Serializable {
    /**
     * 创建开始时间戳
     */
    @ApiModelProperty("创建开始时间戳")
    //    @JsonSerialize(using = DateTimeConvertSerializer.class)
    private Date createTimeBegin;
    /**
     * 创建截止时间戳
     */
    @ApiModelProperty("创建截止时间戳")
    //    @JsonSerialize(using = DateTimeConvertSerializer.class)
    private Date createTimeEnd;
    /**
     * 页面大小
     */
    @ApiModelProperty("页面大小")
    @Getter(AccessLevel.NONE)
    private Integer pageSize;

    /**
     * 页码
     */
    @ApiModelProperty("页码")
    @Getter(AccessLevel.NONE)
    private Integer pageNum;

    public Integer getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }

    public Integer getPageNum() {
        return pageNum == null ? 1 : pageNum;
    }
}
