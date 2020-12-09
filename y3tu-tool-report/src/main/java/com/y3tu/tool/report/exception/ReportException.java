package com.y3tu.tool.report.exception;

import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.IError;

/**
 * 报表异常
 *
 * @author y3tu
 */
public class ReportException extends BaseException {

    public ReportException() {
        super();
    }

    public ReportException(String message) {
        super(message);
    }

    public ReportException(Throwable e) {
        super(e);
    }

    public ReportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public ReportException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public ReportException(IError error) {
        super(error);
    }

    public ReportException(String message, IError error) {
        super(message, error);
    }

    public ReportException(String message, Throwable cause, IError error) {
        super(message, cause, error);
    }

    public ReportException(Throwable cause, IError error) {
        super(cause, error);
    }
}
