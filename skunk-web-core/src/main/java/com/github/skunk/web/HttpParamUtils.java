package com.github.skunk.web;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;

import com.github.skunk.core.collectors.CollectorUtils;
import com.github.skunk.core.filter.ListFilter;
import com.github.skunk.core.filter.ListFilterHelper;
import com.github.skunk.core.filter.PageFilter;
import com.github.skunk.core.filter.PageFilterHelper;
import com.github.skunk.core.filter.SortOrder;

/**
 * 请求参数工具类
 *
 * @author nanfeng
 * @date 2019年12月29日
 * @since 0.0.1
 */
public class HttpParamUtils {

    public static String transCoding(String value) {
        return new String(value.getBytes(ISO_8859_1), UTF_8);
    }

    /**
     * @param currentPage
     * @param pageSize
     * @return
     */
    public static String getPageOffset(int currentPage, int pageSize) {
        int offset = (currentPage - 1) * pageSize;
        return offset < 0 ? 0 + "" : offset + "";
    }

    /**
     * HttpServletRequest parameter to hashmap<String, String>
     *
     * @param request
     * @return
     */
    public static HashMap<String, String> requestToMap(@NotBlank HttpServletRequest request) {
        HashMap<String, String> params = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        Collections.list(names).stream().filter(name -> StringUtils.isNotBlank(name)).forEach(name -> {
            String parameter = request.getParameter(name);
            if (StringUtils.isNotBlank(parameter)) {
                params.put(name, request.getParameter(name));
            }
        });
        return params;
    }

    /**
     * HttpServletRequest parameter to hashmap<String, Object>
     *
     * @param request
     * @return
     */
    public static HashMap<String, Object> requestToMapValueObject(@NotBlank HttpServletRequest request) {
        HashMap<String, Object> params = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        Collections.list(names).stream().filter(name -> StringUtils.isNotBlank(name)).forEach(name -> {
            params.put(name, request.getParameter(name));
        });
        return params;
    }

    /**
     * 获取分页大小
     *
     * @param params
     * @return
     */
    public static int pageSize(Map<String, Object> params) {
        if (CollectorUtils.isEmpty(params) || !params.containsKey(PageFilter.PAGE_SIZE)) {
            return PageFilter.DEFAULT_PAGE_SIZE;
        }

        Object pageSizeObj = params.get(PageFilter.PAGE_SIZE);
        if (!Objects.isNull(pageSizeObj) && StringUtils.isNoneBlank(pageSizeObj.toString())) {
            return Integer.parseInt(pageSizeObj.toString());
        }

        return PageFilter.DEFAULT_PAGE_SIZE;
    }

    /**
     * 当前页号
     *
     * @param params
     * @return
     */
    public static int pageNo(Map<String, Object> params) {
        if (CollectorUtils.isEmpty(params) || !params.containsKey(PageFilter.PAGE_NO)) {
            return PageFilter.DEFAULT_PAGE_INDEX;
        }

        Object pageNoObj = params.get(PageFilter.PAGE_NO);

        if (!Objects.isNull(pageNoObj) && StringUtils.isNoneBlank(pageNoObj.toString())) {

            int pageNo = Integer.parseInt(pageNoObj.toString());

            return pageNo <= 0 ? PageFilter.DEFAULT_PAGE_INDEX : pageNo;

        }
        return PageFilter.DEFAULT_PAGE_INDEX;
    }

    /**
     * 根据预订的规则获取分页参数
     * eg:
     * http://localhost:9020/api/role/list?sortField=roleName&amp;sortOrder=descend&amp;orderBys[]=roleName:descend
     *
     * @param request
     * @return
     */
    public static ListFilter listFilter(@NotBlank HttpServletRequest request) {
        HashMap<String, Object> params = HttpParamUtils.requestToMapValueObject(request);
        ListFilter listFilter = ListFilterHelper.builder()
            .params(params)
            .build();

        if (!Objects.isNull(params.get(SortOrder.ORDER_BY_KEY)) && StringUtils.isNotBlank(params.get(SortOrder.ORDER_BY_KEY).toString())) {
            listFilter.setOrderField(params.get(SortOrder.ORDER_BY_KEY).toString());
            listFilter.setOrderMethod(SortOrder.Type.valueCode(SortOrder.ORDER_METHOD_KEY));

        }
        return listFilter;
    }

    /**
     * 根据预订的规则获取分页参数
     * eg:
     * http://localhost:9020/api/role/page?pageNo=1&amp;pageSize=10&amp;sortField=roleName&amp;sortOrder=descend&amp;orderBys[]=roleName:descend
     *
     * @param request
     * @return
     */
    public static PageFilter pageFilter(@NotBlank HttpServletRequest request) {
        Map<String, Object> params = HttpParamUtils.requestToMapValueObject(request);
        int pageNo = HttpParamUtils.pageNo(params);
        int pageSize = HttpParamUtils.pageSize(params);

        PageFilter pageFilter = PageFilterHelper.builder()
            .pageNo(pageNo)
            .pageSize(pageSize)
            .params(params)
            .build();

        Object value = params.get(SortOrder.ORDER_BY_KEY);

        if (!Objects.isNull(value)) {
            String paramValue = value.toString();
            if (StringUtils.isNotBlank(paramValue)) {
                pageFilter.setOrderField(paramValue);
                pageFilter.setOrderMethod(SortOrder.Type.valueCode(SortOrder.ORDER_METHOD_KEY));

            }
        }
        return pageFilter;
    }
}
