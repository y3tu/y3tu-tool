package com.y3tu.tool.filesystem;


import com.y3tu.tool.core.exception.BaseException;
import com.y3tu.tool.core.exception.ErrorEnum;
import com.y3tu.tool.core.exception.ExceptionUtil;
import com.y3tu.tool.core.util.StrUtil;

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

    public FileSystemException(ErrorEnum error) {
        super();
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public FileSystemException(String message, ErrorEnum error) {
        this(message);
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public FileSystemException(String message, Throwable cause, ErrorEnum error) {
        this(message, cause);
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public FileSystemException(Throwable cause, ErrorEnum error) {
        this(cause);
        this.code = error.getCode();
        this.message = error.getMessage();
    }

}
