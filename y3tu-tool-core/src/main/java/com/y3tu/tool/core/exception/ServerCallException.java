package com.y3tu.tool.core.exception;

/**
 * 服务调用异常
 *
 * @author y3tu
 * @date 2019-05-12
 */
public class ServerCallException extends BaseException {
    public ServerCallException(String message) {
        super(message);
    }

    public ServerCallException(Throwable cause) {
        super(cause);
    }

    public ServerCallException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerCallException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public ServerCallException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public ServerCallException(IError error) {
        super(error);
    }

    public ServerCallException(String message, IError error) {
        super(message, error);
    }

    public ServerCallException(String message, Throwable cause, IError error) {
        super(message, cause, error);
    }

    public ServerCallException(Throwable cause, IError error) {
        super(cause, error);
    }
}
