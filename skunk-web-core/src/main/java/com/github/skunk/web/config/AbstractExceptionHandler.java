package com.github.skunk.web.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.github.skunk.core.exception.BusinessException;
import com.github.skunk.core.exception.NotFoundResourceException;
import com.github.skunk.core.exception.UnrealizedException;
import com.github.skunk.core.validation.exception.ValidateIllegalArgumentException;
import com.github.skunk.core.validation.exception.ValidateIndexOutOfBoundsException;
import com.github.skunk.core.validation.exception.ValidateNullPointerException;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常拦截定义
 * 若业务系统需要此功能，请重新定义继承此类，然后在类上加上 @RestControllerAdvice 注解，那么业务系统就生效
 *
 * @author nanfeng
 * @since 0.0.1
 */
@Slf4j
public class AbstractExceptionHandler extends ResponseEntityExceptionHandler {

    protected static final String MESSAGE_KEY = "message";
    protected static final String HTTP_STATUS_KEY = "status";

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(MESSAGE_KEY, ex.getMessage());
        return new ResponseEntity<>(parameters, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 业务异常
     *
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = { BusinessException.class })
    protected ResponseEntity<Map<String, Object>> businessException(BusinessException ex) {
        log.error(ex.getMessage(), ex);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(MESSAGE_KEY, ex.getMessage());
        return new ResponseEntity<>(parameters, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 功能未实现
     *
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = { UnrealizedException.class })
    protected ResponseEntity<Map<String, Object>> unrealizedException(UnrealizedException ex) {
        log.warn(ex.getMessage(), ex);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(MESSAGE_KEY, ex.getMessage());
        return new ResponseEntity<>(parameters, HttpStatus.BAD_REQUEST);
    }

    /**
     * 输入参数错误
     *
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = { ValidateIllegalArgumentException.class, ValidateIndexOutOfBoundsException.class, ValidateNullPointerException.class,
        NotFoundResourceException.class })
    protected ResponseEntity<Map<String, Object>> validateIllegalException(RuntimeException ex) {
        log.warn(ex.getMessage(), ex);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(MESSAGE_KEY, ex.getMessage());
        return new ResponseEntity<>(parameters, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    }

    /**
     * @param ex
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<String> constraintViolationException(ConstraintViolationException ex) {
        String message = "";
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            message += violation.getMessage() + ", ";
        }
        log.warn(ex.getMessage(), ex);
        return new ResponseEntity<>(message, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
    }

}