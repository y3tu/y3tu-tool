package com.y3tu.tool.web.annotation;

import com.y3tu.tool.web.redis.RedisUtils;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author y3tu
 * @date 2018/10/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RedisUtils.class)
public @interface EnableRedisTool {
}
