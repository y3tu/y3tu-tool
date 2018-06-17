package com.y3tu.tool.core.exception;

/**
 * 工具类异常
 *
 * @author y3tu
 * @date 2018/6/16
 */
public class UtilException extends RuntimeException {
    public UtilException(String message) {
        super(message);
    }

    public UtilException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
