package com.y3tu.tool.core.exception;

/**
 * 服务异常
 * @author y3tu
 * @date 2018/10/2
 */
public class ServerException extends BaseException{
    public ServerException() {
        super();
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable e) {
        super(e);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public ServerException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public ServerException(IError error) {
        super();
        this.errorMessage = null;
        this.error = error;
    }

    public ServerException(String message, IError error) {
        this(message);
        this.errorMessage = message;
        this.error = error;
    }

    public ServerException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.errorMessage = message;
        this.error = error;
    }

    public ServerException(Throwable cause, IError error) {
        this(cause);
        this.errorMessage = null;
        this.error = error;
    }
}
