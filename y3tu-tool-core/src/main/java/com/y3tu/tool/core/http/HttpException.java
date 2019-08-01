package com.y3tu.tool.core.http;

import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.IError;
import com.y3tu.tool.core.exception.ExceptionUtil;

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
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public HttpException(String message, IError error) {
        this(message);
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public HttpException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public HttpException(Throwable cause, IError error) {
        this(cause);
        this.code = error.getCode();
        this.message = error.getMessage();
    }
}
