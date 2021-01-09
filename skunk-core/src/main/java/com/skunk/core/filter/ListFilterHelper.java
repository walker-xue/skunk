package com.skunk.core.filter;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.skunk.core.utils.ColumnPropertyUtils;
import com.skunk.core.utils.Objects2;
import com.skunk.core.utils.String2Utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 分页插件实现
 *
 * @author walker
 * @since 2019年5月13日
 */
@Getter
@Setter
@Builder
public class ListFilterHelper implements ListFilter {

    /* 排序方式 */
    protected SortOrder.Type orderMethod;

    /* 排序字段 */
    protected String orderField;

    /* 查询参数 */
    protected Map<String, Object> params;

    public ListFilterHelper(SortOrder.Type orderMethod, String orderField, Map<String, Object> params) {
        this.orderMethod = orderMethod;
        this.orderField = orderField;
        this.params = params;
    }

    public ListFilterHelper() {
    }

    /**
     * @return
     */
    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * @param paramKey
     * @return
     */
    @Override
    public Optional<Object> getParamValue(String paramKey) {

        Objects2.requireNotBlank(paramKey);

        return Optional.of(params.get(paramKey));
    }

    /**
     * @param paramKey
     * @return
     */
    @Override
    public Optional<String> getParamValueToString(String paramKey) {

        Objects2.requireNotBlank(paramKey);

        Object o = params.get(paramKey);
        if (Objects.isNull(o))
            return Optional.empty();
        return Optional.of(o.toString());
    }

    /**
     * @param clazz
     * @param <T>
     * @return
     */
    @Override
    public <T> Optional<T> paramsToClass(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        return Objects2.mapToBean(params, clazz);
    }

    /**
     * 前端排序字段转成SQL 排序语句
     *
     * @return
     */
    @Override
    public Optional<String> getOrderBy() {
        if (String2Utils.isBlank(orderField)) {
            return Optional.empty();
        }
        return Optional.of(ColumnPropertyUtils.propertyToColumn(orderField).concat(" " + orderMethod.getSort()));
    }
}
