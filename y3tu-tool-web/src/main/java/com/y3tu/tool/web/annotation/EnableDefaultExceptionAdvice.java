package com.y3tu.tool.web.annotation;

import com.y3tu.tool.web.exception.DefaultExceptionAdvice;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 在启动类上添加该注解来启用默认的统一异常处理
 *
 * @author y3tu
 * @date 2018/10/2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(DefaultExceptionAdvice.class)
public @interface EnableDefaultExceptionAdvice {
}
