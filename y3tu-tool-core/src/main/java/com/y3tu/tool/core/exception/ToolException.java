package com.y3tu.tool.core.exception;


/**
 * 工具类异常
 *
 * @author y3tu
 * @date 2018/6/16
 */
public class ToolException extends BaseException {
    public ToolException() {
        super();
    }

    public ToolException(String message) {
        super(message);
    }

    public ToolException(Throwable e) {
        super(e);
    }

    public ToolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public ToolException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public ToolException(ErrorEnum error) {
        super(error);
    }

    public ToolException(String message, ErrorEnum error) {
        super(message, error);
    }

    public ToolException(String message, Throwable cause, ErrorEnum error) {
        super(message, cause, error);
    }

    public ToolException(Throwable cause, ErrorEnum error) {
        super(cause, error);
    }
}
