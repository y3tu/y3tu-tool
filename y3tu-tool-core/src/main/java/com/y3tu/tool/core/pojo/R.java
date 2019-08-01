package com.y3tu.tool.core.pojo;

import com.y3tu.tool.core.exception.ErrorEnum;
import com.y3tu.tool.core.exception.IError;
import com.y3tu.tool.core.util.StrUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回
 *
 * @author y3tu
 * @date 2018/1/10
 */
@Data
public class R<T> implements Serializable {

    /**
     * 失败错误码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;
    /**
     * 返回结果对象
     */
    private T data;
    /**
     * 返回状态 成功,失败,警告 默认成功
     */
    private Status status = Status.SUCCESS;


    public R() {
    }

    public R(String message) {
        this.message = message;
    }

    public R(T data) {
        this.data = data;
    }

    public R(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public static R success() {
        R r = new R();
        r.setStatus(Status.SUCCESS);
        return r;
    }

    public static R success(Object data) {
        R r = new R();
        r.setStatus(Status.SUCCESS);
        r.setData(data);
        return r;
    }

    public static R success(String msg, Object... data) {
        R r = new R();
        r.setStatus(Status.SUCCESS);
        r.setData(data);
        r.setMessage(msg);
        return r;
    }

    public static R warn() {
        R r = new R();
        r.setStatus(Status.WARN);
        return r;
    }

    public static R warn(String msg, Object... data) {
        R r = new R();
        r.setStatus(Status.WARN);
        r.setData(data);
        r.setMessage(msg);
        return r;
    }

    public static R error() {
        R r = R.error(ErrorEnum.SYSTEM_INTERNAL_ERROR);
        r.setStatus(Status.ERROR);
        return r;
    }

    public static R error(String code, String message) {
        R r = new R();
        r.setStatus(Status.ERROR);
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static R error(String message) {
        R r = new R();
        r.setCode(ErrorEnum.SYSTEM_INTERNAL_ERROR.getCode());
        r.setStatus(Status.ERROR);
        r.setMessage(message);
        return r;
    }

    public static R error(String code, String message, Object data) {
        R r = new R();
        r.setStatus(Status.ERROR);
        r.setCode(code);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static R error(int code, String message, Object data) {
        R r = new R();
        r.setStatus(Status.ERROR);
        r.setCode(String.valueOf(code));
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static R error(String message, IError error) {
        R r = new R();
        r.code = error.getCode();
        if (StrUtil.isNotEmpty(message)) {
            r.message = message;
        } else {
            r.message = error.getMessage();
        }
        r.status = Status.ERROR;
        return r;
    }

    public static R error(IError error) {
        R r = new R();
        r.code = error.getCode();
        r.message = error.getMessage();
        r.status = Status.ERROR;
        return r;
    }

    public static enum Status {
        /**
         * 状态
         */
        SUCCESS,
        WARN,
        ERROR;

        Status() {
        }
    }
}
