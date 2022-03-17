package cn.ve.rest.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ve
 * @date 2021/8/3
 */
@Data
public abstract class BaseQO implements Serializable {

    /**
     * 创建开始时间yyyy-MM-dd HH:mm:ss
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeBegin;
    /**
     * 创建截止时间yyyyMMddHHmmss
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTimeEnd;
    /**
     * 页面大小
     */
    private Integer pageSize;

    /**
     * 页码
     */
    private Integer pageNum;

}
