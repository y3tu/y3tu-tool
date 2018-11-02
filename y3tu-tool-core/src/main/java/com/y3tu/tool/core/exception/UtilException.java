package com.y3tu.tool.core.exception;


/**
 * 工具类异常
 *
 * @author y3tu
 * @date 2018/6/16
 */
public class UtilException extends BaseException {
    public UtilException() {
        super();
    }

    public UtilException(String message) {
        super(message);
    }

    public UtilException(Throwable e) {
        super(e);
    }

    public UtilException(String message, Throwable cause) {
        super(message, cause);
    }

    public UtilException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public UtilException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public UtilException(IError error) {
        super(error);
    }

    public UtilException(String message, IError error) {
        super(message,error);
    }

    public UtilException(String message, Throwable cause, IError error) {
        super(message, cause,error);
    }

    public UtilException(Throwable cause, IError error) {
        super(cause,error);
    }
}
