package com.y3tu.tool.core.exception;

/**
 * 异常枚举接口
 *
 * @author y3tu
 * @date 2019-05-10
 */
public interface IError {

    /**
     * 返回异常code
     *
     * @return
     */
    String getCode();

    /**
     * 返回异常消息
     *
     * @return
     */
    String getMessage();
}
