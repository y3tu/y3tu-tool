package com.y3tu.tool.filesystem;

import cn.hutool.core.util.StrUtil;
import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.ExceptionUtil;
import com.y3tu.tool.core.exception.IError;

/**
 * 文件服务器异常类
 *
 * @author y3tu
 * @date 2018/10/30
 */
public class FileSystemException extends BaseException {
    public FileSystemException() {
        super();
    }

    public FileSystemException(String message) {
        super(message);
    }

    public FileSystemException(Throwable cause) {
        super(ExceptionUtil.getMessage(cause), cause);
    }

    public FileSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSystemException(String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public FileSystemException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }

    public FileSystemException(IError error) {
        super();
        this.errorMessage = null;
        this.error = error;
    }

    public FileSystemException(String message, IError error) {
        this(message);
        this.errorMessage = message;
        this.error = error;
    }

    public FileSystemException(String message, Throwable cause, IError error) {
        this(message, cause);
        this.errorMessage = message;
        this.error = error;
    }

    public FileSystemException(Throwable cause, IError error) {
        this(cause);
        this.errorMessage = null;
        this.error = error;
    }

}
