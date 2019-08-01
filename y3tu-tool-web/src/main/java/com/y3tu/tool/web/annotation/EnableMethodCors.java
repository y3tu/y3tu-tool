package com.y3tu.tool.web.annotation;

import com.y3tu.tool.web.config.CorsConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 允许跨域访问，此注解作用于Controller方法上，允许该方法被跨域调用
 * <p>
 * 也可使用spring 自带的 {@link org.springframework.web.bind.annotation.CrossOrigin} 注解
 * 也可使用自定义全局跨域配置 {@link CorsConfig}
 *
 * @author y3tu
 * @date 2018/10/30
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableMethodCors {
}
