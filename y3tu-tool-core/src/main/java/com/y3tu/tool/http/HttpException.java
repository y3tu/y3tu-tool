package com.y3tu.tool.http;


import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.ExceptionUtil;
import com.y3tu.tool.core.exception.IError;
import com.y3tu.tool.core.util.StrUtil;

/**
 * Http异常
 *
 * @author y3tu
 */
public class HttpException extends BaseException {

    public HttpException() {
        super();
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(Throwable cause) {
        super(ExceptionUtil.getMessage(cause), cause);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public HttpException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }

    public HttpException(IError error) {
        super();
        this.errorMessage = null;
        this.error = error;
    }

    public HttpException(String message, IError error) {
        this(message);
        this.errorMessage = message;
        this.error = error;
    }

    public HttpException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.errorMessage = message;
        this.error = error;
    }

    public HttpException(Throwable cause, IError error) {
        this(cause);
        this.errorMessage = null;
        this.error = error;
    }
}
