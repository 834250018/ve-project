package cn.ve.base.pojo;

import cn.ve.base.util.StringConstant;

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
        super(veResultCode.getCode() + StringConstant.POUND + veResultCode.getMsg());
    }

    public VeBaseException(VeResultCode veResultCode, String additionalString) {
        super(veResultCode.getCode() + StringConstant.POUND + veResultCode.getMsg() + StringConstant.COLON + additionalString);
    }

    public VeBaseException(Integer code, String msg, String additionalString) {
        super(code + StringConstant.POUND + msg + StringConstant.COLON + additionalString);
    }

    public VeBaseException(Integer code, String msg) {
        super(code + StringConstant.POUND + msg);
    }

    public VeBaseException(String code, String message) {
        super(code + StringConstant.POUND + message);
    }

    public VeBaseException(String message) {
        super(500 + StringConstant.POUND + message);
    }

    // 重写此方法返回this,业务异常不打印堆栈信息提高速度
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}