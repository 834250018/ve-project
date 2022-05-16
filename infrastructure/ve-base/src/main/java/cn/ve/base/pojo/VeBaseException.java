package cn.ve.base.pojo;

import cn.ve.base.util.StringConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author ve
 * @date 2018/7/25 15:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VeBaseException extends RuntimeException implements Serializable {

    private int code;
    private String msg;

    private static final long serialVersionUID = 1L;

    public VeBaseException(Throwable cause) {
        super(cause);
        init(500, cause.getMessage());
    }

    private void init(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public VeBaseException(VeResultCode veResultCode) {
        super(veResultCode.getCode() + StringConstant.POUND + veResultCode.getMsg());
        init(veResultCode.getCode(), veResultCode.getMsg());
    }

    public VeBaseException(VeResultCode veResultCode, String additionalString) {
        super(veResultCode.getCode() + StringConstant.POUND + veResultCode.getMsg() + StringConstant.COLON + additionalString);
        init(veResultCode.getCode(), veResultCode.getMsg() + StringConstant.COLON + additionalString);
    }

    public VeBaseException(Integer code, String msg, String additionalString) {
        super(code + StringConstant.POUND + msg + StringConstant.COLON + additionalString);
        init(code, msg + StringConstant.COLON + additionalString);
    }

    public VeBaseException(Integer code, String msg) {
        super(code + StringConstant.POUND + msg);
        init(code, msg);
    }

    public VeBaseException(String message) {
        super(500 + StringConstant.POUND + message);
        init(500, message);
    }

    // 重写此方法返回this,业务异常不打印堆栈信息提高速度
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}