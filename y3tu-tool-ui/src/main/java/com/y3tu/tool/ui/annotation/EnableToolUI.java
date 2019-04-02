package com.y3tu.tool.ui.annotation;


import com.y3tu.tool.ui.config.UiAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启工具UI
 *
 * @author y3tu
 * @date 2019-03-29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(UiAutoConfiguration.class)
public @interface EnableToolUI {
}
