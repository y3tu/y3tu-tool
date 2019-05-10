package com.y3tu.tool.cache.exception;


import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.IError;

/**
 *  序列化异常
 * @author y3tu
 */
public class SerializationException extends BaseException {

    public SerializationException() {
        super();
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(Throwable e) {
        super(e);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public SerializationException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public SerializationException(IError error) {
        super(error);
    }

    public SerializationException(String message, IError error) {
        super(message, error);
    }

    public SerializationException(String message, Throwable cause, IError error) {
        super(message, cause, error);
    }

    public SerializationException(Throwable cause, IError error) {
        super(cause, error);
    }
}
