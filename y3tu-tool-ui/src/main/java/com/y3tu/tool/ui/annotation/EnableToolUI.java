package com.y3tu.tool.ui.annotation;

import com.y3tu.tool.ui.configure.UiAutoConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

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
