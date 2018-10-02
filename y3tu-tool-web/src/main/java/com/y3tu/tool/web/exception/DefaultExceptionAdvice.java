package com.y3tu.tool.web.exception;

import com.y3tu.tool.core.exception.*;
import com.y3tu.tool.web.base.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

/**
 * Desc <p>Controller统一异常advice</p>
 *
 * @author y3tu
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class DefaultExceptionAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public ResponseEntity handleSQLException(SQLException e) {
        log.error("服务运行SQLException异常", e);
        R response = R.error(DefaultError.SQL_EXCEPTION);
        response.setMessage(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 所有异常统一处理
     *
     * @return ResponseEntity
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        log.error("异常", e);
        IError error;
        String message = null;
        if (e instanceof BusinessException) {
            error = ((BusinessException) e).getError();
            message = ((BusinessException) e).getErrorMessage();
        } else if (e instanceof UtilException) {
            error = ((UtilException) e).getError();
            message = ((UtilException) e).getErrorMessage();
        } else if (e instanceof ServerException) {
            error = ((ServerException) e).getError();
            message = ((ServerException) e).getErrorMessage();
        } else {
            error = DefaultError.SYSTEM_INTERNAL_ERROR;
            message = e.getMessage();
        }
        R response = R.error(error);
        response.setMessage(message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity handle(Throwable throwable) {
        RuntimeException runtimeException = ExceptionUtil.wrapRuntime(throwable);
        R response = R.error(DefaultError.SYSTEM_INTERNAL_ERROR);
        response.setMessage(runtimeException.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
