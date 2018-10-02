package com.y3tu.tool.core.exception;


/**
 * 网络异常
 *
 * @author y3tu
 */
public class NetException extends BaseException {
    public NetException() {
        super();
    }

    public NetException(String message) {
        super(message);
    }

    public NetException(Throwable e) {
        super(e);
    }

    public NetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public NetException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public NetException(IError error) {
        super();
        this.errorMessage = null;
        this.error = error;
    }

    public NetException(String message, IError error) {
        this(message);
        this.errorMessage = message;
        this.error = error;
    }

    public NetException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.errorMessage = message;
        this.error = error;
    }

    public NetException(Throwable cause, IError error) {
        this(cause);
        this.errorMessage = null;
        this.error = error;
    }
}
