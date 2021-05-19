package com.y3tu.tool.lowcode.ui.annotation;

import com.y3tu.tool.lowcode.ui.configure.UiAutoConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启工具UI
 *
 * @author y3tu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(UiAutoConfigure.class)
public @interface EnableToolUI {
}
