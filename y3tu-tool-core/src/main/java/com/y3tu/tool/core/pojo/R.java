package com.y3tu.tool.core.pojo;

import com.y3tu.tool.core.exception.IError;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回
 *
 * @author y3tu
 * @date 2018/1/10
 */
@Data
public class R implements Serializable {

    /**
     * 返回消息
     */
    private String message;
    /**
     * 返回结果对象
     */
    private Object result;
    /**
     * 返回状态 成功,失败,警告
     */
    private R.Status status;
    /**
     * 失败错误码
     */
    private String errorCode;
    /**
     * 失败返回消息
     */
    private String errorMessage;

    public R() {
    }

    public R(IError error) {
        this.errorCode = error.getErrorCode();
        this.errorMessage = error.getErrorMessage();
        this.status = Status.ERROR;
    }

    public static R ok() {
        R r = new R();
        r.setStatus(Status.OK);
        return r;
    }

    public static R ok(Object result) {
        R r = new R();
        r.setStatus(Status.OK);
        if (result instanceof String) {
            r.setMessage(result.toString());
        }
        r.setResult(result);
        return r;
    }

    public static R ok(String msg, Object result) {
        R r = new R();
        r.setStatus(Status.OK);
        r.setResult(result);
        r.setMessage(msg);
        return r;
    }

    public static R warn() {
        R r = new R();
        r.setStatus(Status.WARN);
        return r;
    }

    public static R warn(Object result) {
        R r = new R();
        r.setStatus(Status.WARN);
        if (result instanceof String) {
            r.setMessage(result.toString());
        }
        r.setResult(result);
        return r;
    }

    public static R warn(String msg, Object result) {
        R r = new R();
        r.setStatus(Status.WARN);
        r.setResult(result);
        r.setMessage(msg);
        return r;
    }

    public static R error() {
        R r = new R();
        r.setStatus(Status.ERROR);
        return r;
    }

    public static R error(Object result) {
        R r = new R();
        r.setStatus(Status.ERROR);
        if (result instanceof String) {
            r.setMessage(result.toString());
        }
        r.setResult(result);
        return r;
    }

    public static R error(String msg, Object result) {
        R r = new R();
        r.setStatus(Status.ERROR);
        r.setResult(result);
        r.setErrorMessage(msg);
        return r;
    }

    public static R error(String errorCode, String errorMessage, Object result) {
        R r = new R();
        r.setStatus(Status.ERROR);
        r.setErrorCode(errorCode);
        r.setErrorMessage(errorMessage);
        r.setResult(result);
        return r;
    }

    public static R error(int errorCode, String errorMessage, Object result) {
        R r = new R();
        r.setStatus(Status.ERROR);
        r.setErrorCode(String.valueOf(errorCode));
        r.setErrorMessage(errorMessage);
        r.setResult(result);
        return r;
    }


    public static R error(IError error) {
        R r = new R();
        r.errorCode = error.getErrorCode();
        r.errorMessage = error.getErrorMessage();
        r.status = Status.ERROR;
        return r;
    }

    public static enum Status {
        /**
         * 状态
         */
        OK,
        WARN,
        ERROR;

        Status() {
        }
    }
}
