package com.skunk.core.filter;

import com.skunk.core.utils.ColumnPropertyUtils;
import com.skunk.core.utils.ObjectUtils;
import com.skunk.core.utils.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

/**
 * 分页插件实现
 *
 * @author walker
 * @since 2019年5月13日
 */

@Getter
@Setter
@Builder
public class PageFilterHelper implements PageFilter {

    /**
     * 当前页
     */
    private int pageNo;
    /**
     * 每页显示条数
     */
    private int pageSize;

    private boolean notQueryTotalNum;

    private SortOrder.Type orderMethod;

    private String orderField;

    /**
     * 查询参会
     */
    private Map<String, Object> params;

    @Override
    public int getPageNo() {
        return pageNo;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public Object getParamValue(String paramKey) {
        return params.get(paramKey);
    }

    @Override
    public String getParamValueToString(String paramKey) {
        Object o = params.get(paramKey);
        if (Objects.isNull(o))
            return null;
        return o.toString();
    }

    @Override
    public <T> T paramsToObject(Class<T> clazz) {
        return ObjectUtils.mapToObject(this.params, clazz);
    }

    @Override
    public ListFilter listFilter() {

        return ListFilterHelper.builder()
            .params(getParams())
            .orderField(orderField)
            .orderMethod(orderMethod)
            .build();
    }

    /**
     * 前端排序字段转成SQL 排序语句
     *
     * @return
     */
    @Override
    public String getOrderBy() {
        if (StringUtils.isEmpty(orderField)) {
            return "";
        }
        return ColumnPropertyUtils.propertyToColumn(orderField).concat(" " + orderMethod.getSort());
    }

    @Override
    public void setNotQueryTotalNum(Boolean notQueryTotalNum) {
        this.notQueryTotalNum = notQueryTotalNum;
    }

    @Override
    public boolean isQueryTotalNum() {
        return !notQueryTotalNum;
    }

}
