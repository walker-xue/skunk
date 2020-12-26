package com.skunk.web.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.skunk.core.exception.IllegalArgumentException;
import com.skunk.core.utils.SqlInjectionCheckUtils;

/**
 * 防止SQL注入
 */
@Component
public class SqlInjectionFilter implements Filter {

    /**
     * 初始化
     *
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) {

    }

    /**
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        Iterator<?> values = req.getParameterMap().values().iterator();
        if (HttpMethod.valueOf(req.getMethod()).equals(HttpMethod.GET)) {
            if (SqlInjectionCheckUtils.isSqlInjection(req.getRequestURI())) {
                throw new IllegalArgumentException("URL错误");
            }
        }
        while (values.hasNext()) {
            Stream.of((String[]) values.next()).forEach(e -> {
                if (SqlInjectionCheckUtils.isSqlInjection(e)) {
                    throw new IllegalArgumentException("非法参数字符.");
                }
            });
        }
        chain.doFilter(request, response);
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {

    }
}
