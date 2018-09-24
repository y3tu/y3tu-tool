package com.y3tu.tool.web.aspect;


import com.y3tu.tool.web.exception.RException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Redis切面处理类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-07-17 23:30
 */
@Aspect
@Component
@Slf4j
@ConditionalOnProperty(name = "y3tu-tool.redis.enable", havingValue = "true", matchIfMissing = false)
public class RedisAspect {
    /**
     * 是否开启redis缓存  true开启   false关闭
     */
    @Value("${y3tu-tool.redis.open: false}")
    private boolean open;

    @Around("execution(* com.y3tu.tool.web.util.RedisUtils.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object result = null;
        if (open) {
            try {
                result = point.proceed();
            } catch (Exception e) {
                log.error("redis error", e);
                throw new RException("Redis服务异常");
            }
        }
        return result;
    }
}
