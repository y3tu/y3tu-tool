package com.y3tu.tool.cache.layering;

import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.Error;
import com.y3tu.tool.core.exception.ExceptionUtil;
import com.y3tu.tool.core.text.StringUtils;

/**
 * @author y3tu
 * @date 2018/11/3
 */
public class CacheException extends BaseException {

    public CacheException() {
        super();
    }

    public CacheException(String message) {
        super(message);
        errorMessage = message;
    }

    public CacheException(Throwable cause) {
        super(ExceptionUtil.getMessage(cause), cause);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
        errorMessage = message;
    }

    public CacheException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
        errorMessage = StringUtils.format(messageTemplate, params);
    }

    public CacheException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
        errorMessage = StringUtils.format(messageTemplate, params);
    }

    public CacheException(Error error) {
        super();
        this.errorMessage = error.getErrorMessage();
        this.error = error;
    }

    public CacheException(String message, Error error) {
        this(message);
        this.errorMessage = message;
        this.error = error;
    }

    public CacheException(String message, Throwable cause, Error error) {
        this(message, cause);
        this.errorMessage = message;
        this.error = error;
    }

    public CacheException(Throwable cause, Error error) {
        this(cause);
        this.errorMessage = error.getErrorMessage();
        this.error = error;
    }
}
