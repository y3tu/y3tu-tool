package com.y3tu.tool.core.exception;

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

    public String code;
    public String message;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(Throwable cause) {
        super(ExceptionUtil.getMessage(cause), cause);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BaseException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
        message = StrUtil.format(messageTemplate, params);
    }

    public BaseException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
        message = StrUtil.format(messageTemplate, params);
    }

    public BaseException(IError error) {
        super();
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public BaseException(String message, IError error) {
        this(message);
        this.code = error.getCode();
        if (StrUtil.isNotEmpty(message)) {
            this.message = message;
        } else {
            this.message = error.getMessage();
        }
    }

    public BaseException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.code = error.getCode();
        if (StrUtil.isNotEmpty(message)) {
            this.message = message;
        } else {
            this.message = error.getMessage();
        }
    }

    public BaseException(Throwable cause, IError error) {
        this(cause);
        this.code = error.getCode();
        this.message = error.getMessage();
    }

}
