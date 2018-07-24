package com.y3tu.tool.core.collection;


import com.y3tu.tool.core.text.StringUtils;

/**
 * 集合异常
 * @author y3tu
 */
public class CollectionException extends RuntimeException {

    public CollectionException(Throwable wrapped) {
        super(wrapped);
    }

    public CollectionException(String message) {
        super(message);
    }

    public CollectionException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CollectionException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
    }

    public CollectionException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
    }
}
