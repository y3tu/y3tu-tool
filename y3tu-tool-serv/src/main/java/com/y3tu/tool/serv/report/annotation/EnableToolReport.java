package com.y3tu.tool.serv.report.annotation;

import com.y3tu.tool.serv.report.configure.ToolReportConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启报表功能
 *
 * @author y3tu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ToolReportConfigure.class)
public @interface EnableToolReport {
}
