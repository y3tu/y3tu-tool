package com.y3tu.tool.serv.common.exception;

import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.IError;

/**
 * 异常
 *
 * @author y3tu
 */
public class CommonException extends BaseException {

    public CommonException() {
        super();
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(Throwable e) {
        super(e);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public CommonException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public CommonException(IError error) {
        super(error);
    }

    public CommonException(String message, IError error) {
        super(message, error);
    }

    public CommonException(String message, Throwable cause, IError error) {
        super(message, cause, error);
    }

    public CommonException(Throwable cause, IError error) {
        super(cause, error);
    }
}
