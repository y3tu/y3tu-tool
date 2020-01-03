package com.y3tu.tool.core.exception;


/**
 * 默认异常
 *
 * @author y3tu
 */
public enum ErrorEnum implements IError {
    /**
     * 系统内部错误
     */
    SYSTEM_INTERNAL_ERROR("SYSTEM_ERROR", "系统内部错误"),
    /**
     * 服务调用异常
     */
    SERVICE_CALL_ERROR("SERVICE_CALL_ERROR", "服务调用异常"),
    /**
     * 未发现服务
     */
    SERVICE_NOT_FOUND("SERVICE_NOT_FOUND", "未发现服务"),
    /**
     * 服务运行SQLException异常
     */
    SQL_EXCEPTION("SQL_EXCEPTION", "数据库异常"),
    /**
     * 系统繁忙,请稍候再试
     */
    SYSTEM_BUSY("SYSTEM_BUSY_EXCEPTION", "系统繁忙,请稍候再试"),
    /**
     * Tool工具异常
     */
    UTIL_EXCEPTION("TOOL_EXCEPTION", "工具异常");


    String code;
    String message;

    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
