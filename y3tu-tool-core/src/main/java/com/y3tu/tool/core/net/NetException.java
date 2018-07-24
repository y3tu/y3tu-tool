package com.y3tu.tool.core.net;


import com.y3tu.tool.core.text.StringUtils;

/**
 * 网络异常
 * @author y3tu
 */
public class NetException extends RuntimeException {

    public NetException(Throwable wrapped) {
        super(wrapped);
    }

    public NetException(String message) {
        super(message);
    }

    public NetException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public NetException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
    }

    public NetException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
    }
}
