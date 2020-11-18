package com.y3tu.tool.cache.core.serializer;

import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.ExceptionUtil;
import com.y3tu.tool.core.exception.IError;
import com.y3tu.tool.core.util.StrUtil;

/**
 * 序列号异常
 *
 * @author y3tu
 */
public class SerializationException extends BaseException {

    public SerializationException() {
        super();
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(Throwable cause) {
        super(ExceptionUtil.getMessage(cause), cause);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public SerializationException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }

    public SerializationException(IError error) {
        super();
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public SerializationException(String message, IError error) {
        this(message);
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public SerializationException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public SerializationException(Throwable cause, IError error) {
        this(cause);
        this.code = error.getCode();
        this.message = error.getMessage();
    }
}
