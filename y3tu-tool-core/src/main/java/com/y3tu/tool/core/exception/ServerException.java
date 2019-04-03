package com.y3tu.tool.core.exception;

/**
 * 服务异常
 *
 * @author y3tu
 * @date 2018/10/2
 */
public class ServerException extends BaseException {
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

    public ServerException(ErrorEnum error) {
        super(error);
    }

    public ServerException(String message, ErrorEnum error) {
        super(message, error);
    }

    public ServerException(String message, Throwable cause, ErrorEnum error) {
        super(message, cause, error);
    }

    public ServerException(Throwable cause, ErrorEnum error) {
        super(cause, error);
    }
}
