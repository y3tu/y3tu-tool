package com.y3tu.tool.setting;


import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.ExceptionUtil;
import com.y3tu.tool.core.exception.IError;
import com.y3tu.tool.core.text.StringUtils;

/**
 * 设置异常
 *
 * @author xiaoleilu
 */
public class SettingRuntimeException extends BaseException {

    public SettingRuntimeException() {
        super();
    }

    public SettingRuntimeException(String message) {
        super(message);
    }

    public SettingRuntimeException(Throwable cause) {
        super(ExceptionUtil.getMessage(cause), cause);
    }

    public SettingRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SettingRuntimeException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
    }

    public SettingRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
    }

    public SettingRuntimeException(IError error) {
        super();
        this.errorMessage = null;
        this.error = error;
    }

    public SettingRuntimeException(String message, IError error) {
        this(message);
        this.errorMessage = message;
        this.error = error;
    }

    public SettingRuntimeException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.errorMessage = message;
        this.error = error;
    }

    public SettingRuntimeException(Throwable cause, IError error) {
        this(cause);
        this.errorMessage = null;
        this.error = error;
    }
}
