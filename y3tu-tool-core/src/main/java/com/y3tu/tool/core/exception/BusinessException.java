package com.y3tu.tool.core.exception;

/**
 * 业务异常
 *
 * @author y3tu
 * @date 2018/10/2
 */
public class BusinessException extends BaseException {
    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable e) {
        super(e);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public BusinessException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public BusinessException(IError error) {
        super(error);
    }

    public BusinessException(String message, IError error) {
        super(message, error);
    }

    public BusinessException(String message, Throwable cause, IError error) {
        super(message, cause, error);
    }

    public BusinessException(Throwable cause, IError error) {
        super(cause, error);
    }

}
