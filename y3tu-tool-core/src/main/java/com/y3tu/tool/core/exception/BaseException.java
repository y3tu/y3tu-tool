package com.y3tu.tool.core.exception;

import com.y3tu.tool.core.text.StringUtils;
import lombok.Data;

/**
 * 基本异常
 *
 * @author y3tu
 * @date 2018/10/2
 */
@Data
public class BaseException extends RuntimeException {

    public IError error = DefaultError.SYSTEM_INTERNAL_ERROR;
    public String errorMessage = DefaultError.SYSTEM_INTERNAL_ERROR.getErrorMessage();

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
        errorMessage = message;
    }

    public BaseException(Throwable cause) {
        super(ExceptionUtil.getMessage(cause), cause);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        errorMessage = message;
    }

    public BaseException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
        errorMessage = StringUtils.format(messageTemplate, params);
    }

    public BaseException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
        errorMessage = StringUtils.format(messageTemplate, params);
    }

    public BaseException(IError error) {
        super();
        this.errorMessage = error.getErrorMessage();
        this.error = error;
    }

    public BaseException(String message, IError error) {
        this(message);
        this.errorMessage = message;
        this.error = error;
    }

    public BaseException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.errorMessage = message;
        this.error = error;
    }

    public BaseException(Throwable cause, IError error) {
        this(cause);
        this.errorMessage = error.getErrorMessage();
        this.error = error;
    }

}
