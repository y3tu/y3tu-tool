package com.y3tu.tool.poi.exceptions;


import com.y3tu.tool.core.exception.ExceptionUtil;
import com.y3tu.tool.core.text.StringUtils;

/**
 * POI异常
 *
 * @author xiaoleilu
 */
public class POIException extends RuntimeException {
    private static final long serialVersionUID = 2711633732613506552L;

    public POIException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public POIException(String message) {
        super(message);
    }

    public POIException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
    }

    public POIException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public POIException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
    }
}
