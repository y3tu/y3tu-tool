package com.y3tu.tool.web.annotation;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

/**
 * 该注解用于Controller的方法上，把方法名映射为请求路径
 *
 * @author y3tu
 * @date 2018/10/27
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ResponseBody
public @interface MethodMapping {

    /**
     * 定义请求的方法类型 默认为POST
     *
     * @return
     */
    public abstract RequestMethod[] method() default RequestMethod.GET;

    /**
     * 请求地址 用于类似/get/{id}这种
     * @return
     */
    String value() default "";
}
