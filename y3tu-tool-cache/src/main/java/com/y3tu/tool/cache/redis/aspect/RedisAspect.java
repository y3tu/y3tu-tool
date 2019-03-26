package com.y3tu.tool.cache.redis.aspect;


import com.y3tu.tool.cache.redis.annotation.registrar.EnableRedisToolRegistrar;
import com.y3tu.tool.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Redis切面处理类
 *
 * @author y3tu
 * @date 2018/10/22
 */
@Aspect
@Component
@Slf4j
public class RedisAspect {

    @Around("execution(* com.y3tu.tool.web.redis.template.RedisRepository.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        if (EnableRedisToolRegistrar.redisAspectOpen) {
            try {
                result = point.proceed();
            } catch (Exception e) {
                log.error("redis error", e);
                throw new BusinessException("Redis服务异常");
            }
        }
        return result;
    }
}
