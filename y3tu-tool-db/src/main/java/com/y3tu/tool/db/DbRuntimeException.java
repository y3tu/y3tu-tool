package com.y3tu.tool.db;


import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.IError;

/**
 * 数据库异常
 *
 * @author xiaoleilu
 */
public class DbRuntimeException extends BaseException {
    public DbRuntimeException() {
        super();
    }

    public DbRuntimeException(String message) {
        super(message);
    }

    public DbRuntimeException(Throwable e) {
        super(e);
    }

    public DbRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbRuntimeException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public DbRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public DbRuntimeException(IError error) {
        super();
        this.errorMessage = null;
        this.error = error;
    }

    public DbRuntimeException(String message, IError error) {
        this(message);
        this.errorMessage = message;
        this.error = error;
    }

    public DbRuntimeException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.errorMessage = message;
        this.error = error;
    }

    public DbRuntimeException(Throwable cause, IError error) {
        this(cause);
        this.errorMessage = null;
        this.error = error;
    }
}
