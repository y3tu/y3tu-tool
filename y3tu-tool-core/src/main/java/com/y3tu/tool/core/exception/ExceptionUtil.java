package com.y3tu.tool.core.exception;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * 异常工具类
 * @see cn.hutool.core.exceptions.ExceptionUtil
 * @author y3tu
 */
public class ExceptionUtil extends cn.hutool.core.exceptions.ExceptionUtil {
    /**
     * 根据异常获取异常信息
     *
     * @param e 当前异常
     * @return 组装后的异常消息
     */
    public static String getFormatMessage(Exception e) {
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        Integer lineNumber = stackTraceElement.getLineNumber();
        return "异常发生处：" + className + "." + methodName + " 第" + lineNumber + " 行\n异常简要信息：" + e.getLocalizedMessage();
    }

    /**
     * 获取堆栈信息
     *
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }
}
