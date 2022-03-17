package cn.ve.base.pojo;

import java.io.Serializable;

/**
 * @author ve
 * @date 2018/7/25 15:59
 */
public class VeBaseException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public VeBaseException(Throwable cause) {
        super(cause);
    }

    public VeBaseException(VeResultCode veResultCode) {
        super(veResultCode.getCode() + "#" + veResultCode.getMsg());
    }

    public VeBaseException(VeResultCode veResultCode, String additionalString) {
        super(veResultCode.getCode() + "#" + veResultCode.getMsg() + ":" + additionalString);
    }

    public VeBaseException(Integer code, String msg, String additionalString) {
        super(code + "#" + msg + ":" + additionalString);
    }

    public VeBaseException(Integer code, String msg) {
        super(code + "#" + msg);
    }

    public VeBaseException(String code, String message) {
        super(code + "#" + message);
    }

    public VeBaseException(String message) {
        super(500 + "#" + message);
    }

    // 重写此方法返回this,业务异常不打印堆栈信息提高速度
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}