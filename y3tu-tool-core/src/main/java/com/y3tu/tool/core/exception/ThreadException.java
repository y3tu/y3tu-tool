package com.y3tu.tool.core.exception;

/**
 * @author y3tu
 * @date 2018/5/11
 */
public class ThreadException extends Exception {
    public ThreadException(String message) {
        super(message);
    }

    public ThreadException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
