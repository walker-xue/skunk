package com.github.skunk.web.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lombok.extern.slf4j.Slf4j;

/**
 * 拦截Response数据包，并且打印到log中，方便调试
 *
 * @author nanfeng
 * @since 2.0.0
 */
@Slf4j
@ControllerAdvice
public class ResponseBodyLoggerAdvisor implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        boolean assignableFrom = AbstractJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
        boolean annotationPresent = returnType.getMethod().isAnnotationPresent(ResponseBody.class);
        return assignableFrom || annotationPresent;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
        Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 响应值转JSON串输出到日志系统
        if (log.isDebugEnabled()) {
            log.debug("=> {}: {}", request.getURI(), JSON.toJSONString(body, SerializerFeature.UseSingleQuotes));
        }
        return body;
    }

}