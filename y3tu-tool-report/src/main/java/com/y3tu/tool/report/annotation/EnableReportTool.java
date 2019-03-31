package com.y3tu.tool.report.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动报表工具
 *
 * @author y3tu
 * @date 2018/10/25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({EnableReportToolRegistrar.class})
public @interface EnableReportTool {
}
