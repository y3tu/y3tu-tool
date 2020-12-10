package com.y3tu.tool.web.exception;

import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.IError;

/**
 * 上传下载异常
 *
 * @author y3tu
 */
public class UpDownLoadException extends BaseException {
    public UpDownLoadException() {
        super();
    }

    public UpDownLoadException(String message) {
        super(message);
    }

    public UpDownLoadException(Throwable e) {
        super(e);
    }

    public UpDownLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpDownLoadException(String messageTemplate, Object... params) {
        super(messageTemplate, params);
    }

    public UpDownLoadException(Throwable throwable, String messageTemplate, Object... params) {
        super(throwable, messageTemplate, params);
    }

    public UpDownLoadException(IError error) {
        super(error);
    }

    public UpDownLoadException(String message, IError error) {
        super(message, error);
    }

    public UpDownLoadException(String message, Throwable cause, IError error) {
        super(message, cause, error);
    }

    public UpDownLoadException(Throwable cause, IError error) {
        super(cause, error);
    }
}
