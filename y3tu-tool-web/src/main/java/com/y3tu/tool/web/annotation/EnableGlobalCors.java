package com.y3tu.tool.web.annotation;

import com.y3tu.tool.web.config.CorsConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 允许全局跨域访问
 * @author y3tu
 * @date 2019-04-01
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(CorsConfig.class)
public @interface EnableGlobalCors {
}
