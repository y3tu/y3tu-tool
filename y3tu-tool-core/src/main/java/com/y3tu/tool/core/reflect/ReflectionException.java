package com.y3tu.tool.core.reflect;


import com.y3tu.tool.core.text.StringUtils;

/**
 * 反射异常
 * @author y3tu
 */
public class ReflectionException extends RuntimeException {

    public ReflectionException(Throwable wrapped) {
        super(wrapped);
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ReflectionException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
    }

    public ReflectionException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
    }
}
