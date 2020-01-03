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
    SYSTEM_INTERNAL_ERROR("SYSTEM-ERROR", "系统内部错误"),
    /**
     * 服务调用异常
     */
    SERVICE_CALL_ERROR("SERVICE-CALL-ERROR", "服务调用异常"),
    /**
     * 未发现服务
     */
    SERVICE_NOT_FOUND_ERROR("SERVICE-NOT-FOUND-ERROR", "未发现服务"),
    /**
     * 服务运行SQLException异常
     */
    SQL_EXCEPTION("SQL-ERROR", "数据库异常"),
    /**
     * 系统繁忙,请稍候再试
     */
    SYSTEM_BUSY_ERROR("SYSTEM-BUSY-ERROR", "系统繁忙,请稍候再试"),
    /**
     * Tool工具异常
     */
    UTIL_ERROR("TOOL-ERROR", "工具异常");


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
