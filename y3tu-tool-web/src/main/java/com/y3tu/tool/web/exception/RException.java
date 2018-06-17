package com.y3tu.tool.web.exception;

import lombok.Data;

/**
 * 自定义异常
 *
 * @author y3tu
 * @date 2018/1/18
 */
@Data
public class RException extends RuntimeException {

    private String msg;
    private int code = 500;

    public RException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public RException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public RException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public RException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}
