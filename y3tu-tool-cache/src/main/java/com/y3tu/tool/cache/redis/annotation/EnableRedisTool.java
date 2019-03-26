package com.y3tu.tool.cache.redis.annotation;

import com.y3tu.tool.cache.redis.annotation.registrar.EnableRedisToolRegistrar;
import com.y3tu.tool.cache.redis.aspect.LimitAspect;
import com.y3tu.tool.cache.redis.aspect.RedisAspect;
import com.y3tu.tool.cache.redis.config.RedisConfig;
import com.y3tu.tool.cache.redis.template.RedisRepository;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 需要在自己的项目中配置好redis
 * 此工具只是简化redis的调用
 *
 * @author y3tu
 * @date 2018/10/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({EnableRedisToolRegistrar.class, RedisConfig.class, RedisAspect.class, LimitAspect.class, RedisRepository.class,})
public @interface EnableRedisTool {
    /**
     * 是否开始redis切面控制
     * 默认开启
     *
     * @return
     */
    boolean aspectOpen() default true;


}
