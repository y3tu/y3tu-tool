package com.y3tu.tool.core.exception;

import com.y3tu.tool.core.text.StringUtils;

/**
 * 校验异常
 *
 * @author y3tu
 * @date 2018/6/26
 */
public class ValidatorException extends StatefulException {
    public ValidatorException() {
    }

    public ValidatorException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
