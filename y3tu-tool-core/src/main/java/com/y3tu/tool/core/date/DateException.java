package com.y3tu.tool.core.date;

import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.ExceptionUtil;
import com.y3tu.tool.core.exception.IError;
import com.y3tu.tool.core.text.StringUtils;

/**
 * 时间异常
 *
 * @author y3tu
 */
public class DateException extends BaseException {
    public DateException() {
        super();
    }

    public DateException(String message) {
        super(message);
    }

    public DateException(Throwable cause) {
        super(ExceptionUtil.getMessage(cause), cause);
    }

    public DateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
    }

    public DateException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
    }

    public DateException(IError error) {
        super();
        this.errorMessage = null;
        this.error = error;
    }

    public DateException(String message, IError error) {
        this(message);
        this.errorMessage = message;
        this.error = error;
    }

    public DateException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.errorMessage = message;
        this.error = error;
    }

    public DateException(Throwable cause, IError error) {
        this(cause);
        this.errorMessage = null;
        this.error = error;
    }
}
