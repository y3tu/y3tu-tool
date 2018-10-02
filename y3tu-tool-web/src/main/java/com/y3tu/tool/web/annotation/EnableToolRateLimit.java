package com.y3tu.tool.web.annotation;

import com.y3tu.tool.web.config.LimitRaterInterceptor;
import com.y3tu.tool.web.limit.RedisRaterLimiter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 在启动类上添加该注解来启用工具限流
 *
 * @author y3tu
 * @date 2018/10/2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({LimitRaterInterceptor.class, RedisRaterLimiter.class})
public @interface EnableToolRateLimit {
}
