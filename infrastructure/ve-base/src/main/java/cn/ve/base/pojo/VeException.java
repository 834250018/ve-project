package cn.ve.base.pojo;

import java.io.Serializable;

/**
 * @author ve
 * @date 2018/7/25 15:59
 */
public class VeException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public VeException(VeErrorCode VEErrorCode) {
        super(VEErrorCode.getStatus() + "#" + VEErrorCode.getMsg());
    }

    public VeException(VeErrorCode VEErrorCode, String additionalString) {
        super(VEErrorCode.getStatus() + "#" + VEErrorCode.getMsg() + ":" + additionalString);
    }

    public VeException(Integer code, String msg, String additionalString) {
        super(code + "#" + msg + ":" + additionalString);
    }

    public VeException(Integer code, String msg) {
        super(code + "#" + msg);
    }

    public VeException(String code, String message) {
        super(code + "#" + message);
    }

    public VeException(String message) {
        super(500 + "#" + message);
    }

    // 重写此方法返回this,业务异常不打印堆栈信息提高速度
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}