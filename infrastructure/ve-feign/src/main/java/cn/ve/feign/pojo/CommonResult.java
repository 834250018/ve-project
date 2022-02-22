package cn.ve.feign.pojo;

import cn.hutool.http.HttpStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommonResult<R> implements Serializable {

    private R data;
    private int code;
    private String msg;

    public static <R> CommonResult<R> success(R data) {
        CommonResult<R> result = new CommonResult<>();
        result.setCode(HttpStatus.HTTP_OK);
        result.setData(data);
        return result;
    }

    public static CommonResult<?> success(int code, String msg) {
        CommonResult<?> result = new CommonResult<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

}
