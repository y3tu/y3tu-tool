package com.y3tu.tool.web.aspect;

import cn.hutool.core.date.TimeInterval;
import com.y3tu.tool.core.date.DateUtil;
import com.y3tu.tool.core.exception.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Controller拦截器，主要获取一次请求包含的信息和处理时间
 *
 * @author lengleng
 * @date 2017/12/15
 * Controller 增强
 */

@Slf4j
@Aspect
@Component
public class ControllerAop {

    /**
     * 警告阈值 单位毫秒
     */
    private int threshold = 3000;

    @Pointcut("execution(public com.y3tu.tool.core.pojo.R *(..))")
    public void pointCutR() {
    }

    /**
     * 拦截器具体实现
     *
     * @param pjp 切点 所有返回对象R
     * @return R  结果包装
     */
    @Around("pointCutR()")
    public Object methodRHandler(ProceedingJoinPoint pjp) throws Throwable {
        return methodHandler(pjp);
    }


    @Pointcut("execution(public com.baomidou.mybatisplus.extension.plugins.pagination.Page *(..))")
    public void pointCutPage() {
    }

    /**
     * 拦截器具体实现
     *
     * @param pjp 切点 所有返回对象Page
     * @return R  结果包装
     */
    @Around("pointCutPage()")
    public Object methodPageHandler(ProceedingJoinPoint pjp) throws Throwable {
        return methodHandler(pjp);
    }

    private Object methodHandler(ProceedingJoinPoint pjp) throws Throwable {
        Throwable failed = null;

        //开始计时
        TimeInterval timer = DateUtil.timer();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("CLASS_METHOD : " + pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName());
        log.info("ARGS : " + Arrays.toString(pjp.getArgs()));
        log.info("---------------------------");

        Object result;

        try {
            result = pjp.proceed();
        } catch (Exception e) {
            failed = e;
            log.error("request failed...", ExceptionUtil.getFormatMessage(e));
            throw e;
        } finally {
            //计算调用时间
            long duration = timer.intervalMs();
            if (failed != null) {
                log.error(pjp.getSignature() + " use time:" + duration + " ms");
            } else if (duration > threshold) {
                log.warn(pjp.getSignature() + " use time:" + duration + " ms");
            } else {
                log.info(pjp.getSignature() + " use time:" + duration + " ms");
            }
        }

        return result;
    }
}
