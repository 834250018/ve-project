package cn.ve.rest.filter;

import cn.ve.base.pojo.VeBaseException;
import cn.ve.feign.pojo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常切面,restful接口异常不记录,程序内业务异常记录
 *
 * @author ve
 * @date 2019/7/2 18:10
 */
@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonResult<?> handle(Exception e) {
        log.error(e.getMessage(), e);
        return CommonResult.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    @ExceptionHandler(VeBaseException.class)
    @ResponseBody
    public CommonResult<?> handle(VeBaseException e) {
        log.error("异常编码:{} 异常信息: {}", e.getCode(), e.getMsg());
        return CommonResult.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public CommonResult<?> handle(MethodArgumentNotValidException e) {
        return CommonResult.fail(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseBody
    public CommonResult<?> handle(NoHandlerFoundException e) {
        return CommonResult.fail(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public CommonResult<?> handle(HttpRequestMethodNotSupportedException e) {
        return CommonResult.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

}
