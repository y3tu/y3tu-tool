package com.y3tu.tool.core.exceptions;

import com.y3tu.tool.core.util.StrUtil;
import lombok.Data;

/**
 * 基本异常
 *
 * @author y3tu
 * @date 2018/10/2
 */
@Data
public class BaseException extends RuntimeException {

    public IError error = Error.SYSTEM_INTERNAL_ERROR;
    public String errorMessage = Error.SYSTEM_INTERNAL_ERROR.getErrorMessage();

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
        super(StrUtil.format(messageTemplate, params));
        errorMessage = StrUtil.format(messageTemplate, params);
    }

    public BaseException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
        errorMessage = StrUtil.format(messageTemplate, params);
    }

    public BaseException(Error error) {
        super();
        this.errorMessage = error.getErrorMessage();
        this.error = error;
    }

    public BaseException(String message, Error error) {
        this(message);
        this.errorMessage = message;
        this.error = error;
    }

    public BaseException(String message, Throwable cause, Error error) {
        this(message, cause);
        this.errorMessage = message;
        this.error = error;
    }

    public BaseException(Throwable cause, Error error) {
        this(cause);
        this.errorMessage = error.getErrorMessage();
        this.error = error;
    }

}
