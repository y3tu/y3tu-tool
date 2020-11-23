package com.y3tu.tool.report.annotation;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.*;

/**
 * 开始报表功能
 *
 * @author y3tu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@ComponentScan("com.y3tu.tool.report")
public @interface EnableToolReport {
}
