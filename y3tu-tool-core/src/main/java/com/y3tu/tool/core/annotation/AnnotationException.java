package com.y3tu.tool.core.annotation;


import com.y3tu.tool.core.text.StringUtils;

/**
 * 注解异常
 * @author y3tu
 */
public class AnnotationException extends RuntimeException {

    public AnnotationException(Throwable wrapped) {
        super(wrapped);
    }

    public AnnotationException(String message) {
        super(message);
    }

    public AnnotationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public AnnotationException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
    }

    public AnnotationException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
    }
}
