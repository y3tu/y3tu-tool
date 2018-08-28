package com.y3tu.tool.web.exception;

import com.y3tu.tool.web.base.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * 自定义异常处理类
 *
 * @author y3tu
 * @date 2018/1/18
 */
@Configuration
@RestControllerAdvice
@Slf4j
@ConditionalOnProperty(value = "y3tu-tool.exceptionHandler.enable",havingValue = "true",matchIfMissing = false)
public class RExceptionHandler {

    /**
     * 自定义异常
     */
    @ExceptionHandler(RException.class)
    public R handleRRException(RException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMsg());
    }

    @ExceptionHandler(GridException.class)
    public R handleAuthorizationException(GridException e) {
        log.error(e.getMessage(), e);
        return R.warn(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public R handleAccessDeniedException(GridException e) {
        log.error(e.getMessage(), e);
        return R.warn(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error();
    }
}
