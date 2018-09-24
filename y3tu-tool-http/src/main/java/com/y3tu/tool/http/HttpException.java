package com.y3tu.tool.http;


import com.y3tu.tool.core.text.StringUtils;

/**
 * Http异常
 *
 * @author y3tu
 */
public class HttpException extends RuntimeException {

    public HttpException(Throwable wrapped) {
        super(wrapped);
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public HttpException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
    }

    public HttpException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
    }
}
