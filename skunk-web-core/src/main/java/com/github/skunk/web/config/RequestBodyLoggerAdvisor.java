package com.github.skunk.web.config;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import com.github.skunk.core.utils.JSONUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 拦截Response数据包，并且打印到log中，方便调试
 *
 * @author nanfeng
 * @since 2.0.0
 */
@Slf4j
@ControllerAdvice
public class RequestBodyLoggerAdvisor extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 只处理@RequestBody注解了的参数
        return methodParameter.getParameterAnnotation(RequestBody.class) != null;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
        Class<? extends HttpMessageConverter<?>> converterType) {
        Method method = parameter.getMethod();

        // 自定义日志输出
        if (log.isInfoEnabled()) {
            // 参数对象转JSON字符串
            if (StringHttpMessageConverter.class.isAssignableFrom(converterType)) {
                log.debug("=> {}#{}: {}", parameter.getContainingClass().getSimpleName(), method.getName(), body.toString());
            } else {
                log.debug("=> {}#{}: {}", parameter.getContainingClass().getSimpleName(), method.getName(), JSONUtils.toJson(body));
            }
        }
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}