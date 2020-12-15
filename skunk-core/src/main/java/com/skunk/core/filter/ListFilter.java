package com.skunk.core.filter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

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
    String getOrderBy();

    /**
     * 设置是否排序
     *
     * @return
     */
    default void setIsOrderBy(boolean isOrderBy) {
    }

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
    Object getParamValue(String paramKey);

    /**
     * @param paramKey
     * @return
     */
    String getParamValueToString(String paramKey);


    /**
     * 请求参数转化成对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T paramsToObject(Class<T> clazz);
}
