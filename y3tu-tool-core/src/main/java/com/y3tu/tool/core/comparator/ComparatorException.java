package com.y3tu.tool.core.comparator;


import com.y3tu.tool.core.exception.ExceptionUtil;
import com.y3tu.tool.core.text.StringUtils;

/**
 * 比较异常
 *
 * @author xiaoleilu
 */
public class ComparatorException extends RuntimeException {
    private static final long serialVersionUID = 4475602435485521971L;

    public ComparatorException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public ComparatorException(String message) {
        super(message);
    }

    public ComparatorException(String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params));
    }

    public ComparatorException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ComparatorException(Throwable throwable, String messageTemplate, Object... params) {
        super(StringUtils.format(messageTemplate, params), throwable);
    }
}
