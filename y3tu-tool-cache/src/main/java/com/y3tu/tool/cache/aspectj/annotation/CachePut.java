package com.y3tu.tool.cache.aspectj.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 将对应数据放到缓存中
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CachePut {

    /**
     * 别名 {@link #cacheNames}.
     *
     * @return String[]
     */
    @AliasFor("cacheNames")
    String[] value() default {};

    /**
     * 缓存名称
     *
     * @return String[]
     */
    @AliasFor("value")
    String[] cacheNames() default {};

    /**
     * 描述
     *
     * @return String
     */
    String depict() default "";

    /**
     * 缓存key，支持SpEL表达式
     *
     * @return String
     */
    String key() default "";

    /**
     * 是否忽略在操作缓存中遇到的异常，如反序列化异常，默认true。
     * <p>true: 有异常会输出warn级别的日志，并直接执行被缓存的方法（缓存将失效）</p>
     * <p>false:有异常会输出error级别的日志，并抛出异常</p>
     *
     * @return boolean
     */
    boolean ignoreException() default true;

    /**
     * 一级缓存配置
     *
     * @return FirstCache
     */
    FirstCache firstCache() default @FirstCache();

    /**
     * 二级缓存配置
     *
     * @return SecondaryCache
     */
    SecondaryCache secondaryCache() default @SecondaryCache();
}
