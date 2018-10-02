package com.y3tu.tool.core.exception;


/**
 * 校验异常
 *
 * @author y3tu
 * @date 2018/6/26
 */
public class ValidatorException extends BaseException {
    public ValidatorException() {
        super();
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(Throwable e) {
        super(e);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public ValidatorException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public ValidatorException(IError error) {
        super();
        this.errorMessage = null;
        this.error = error;
    }

    public ValidatorException(String message, IError error) {
        this(message);
        this.errorMessage = message;
        this.error = error;
    }

    public ValidatorException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.errorMessage = message;
        this.error = error;
    }

    public ValidatorException(Throwable cause, IError error) {
        this(cause);
        this.errorMessage = null;
        this.error = error;
    }
}
