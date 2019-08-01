package com.y3tu.tool.core.exception;

/**
 * 依赖异常
 *
 * @author y3tu
 */
public class DependencyException extends BaseException {
    public DependencyException() {
        super();
    }

    public DependencyException(String message) {
        super(message);
    }

    public DependencyException(Throwable e) {
        super(e);
    }

    public DependencyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DependencyException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public DependencyException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public DependencyException(IError error) {
        super(error);
    }

    public DependencyException(String message, IError error) {
        super(message, error);
    }

    public DependencyException(String message, Throwable cause, IError error) {
        super(message, cause, error);
    }

    public DependencyException(Throwable cause, IError error) {
        super(cause, error);
    }
}
