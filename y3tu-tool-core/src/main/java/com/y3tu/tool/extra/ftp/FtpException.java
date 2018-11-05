package com.y3tu.tool.extra.ftp;

import com.y3tu.tool.core.exception.ExceptionUtil;
import com.y3tu.tool.core.util.StrUtil;

/**
 * Ftp异常
 *
 * @author xiaoleilu
 */
public class FtpException extends RuntimeException {
    private static final long serialVersionUID = -8490149159895201756L;

    public FtpException(Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public FtpException(String message) {
        super(message);
    }

    public FtpException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public FtpException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public FtpException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}
