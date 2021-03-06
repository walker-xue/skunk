package com.skunk.core.filter;

import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 列表接口过滤器定义
 *
 * @author walker
 * @since 2019年5月13日
 */
public interface ListFilter {

    /**
     * 获取排序字符串
     *
     * @return
     */
    Optional<String> getOrderBy();

    /**
     * 是否排序
     *
     * @return
     */
    default boolean isOrderBy() {
        return false;
    }

    /**
     * 设置排序字段
     *
     * @return
     */
    void setOrderField(@NotBlank String orderField);

    /**
     * 设置排序方式
     *
     * @param orderMode
     */
    void setOrderMethod(@NotNull SortOrder.Type orderMode);

    /**
     * 获取查询参数
     *
     * @return
     */
    Map<String, Object> getParams();

    /**
     * @param paramKey
     * @return
     */
    Optional<Object> getParamValue(@NotBlank String paramKey);

    /**
     * @param paramKey
     * @return
     */
    Optional<String> getParamValueToString(@NotBlank String paramKey);

    /**
     * 请求参数转化成对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> Optional<T> paramsToClass(@NotNull Class<T> clazz);
}
