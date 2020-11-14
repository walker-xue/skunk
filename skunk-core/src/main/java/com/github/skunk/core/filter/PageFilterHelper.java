package com.github.skunk.core.filter;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.skunk.core.utils.BeanConvertUtils;
import com.github.skunk.core.utils.ColumnPropertyUtils;

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
    public <T> T paramsToObject(Class<T> clazz) {
        return BeanConvertUtils.mapToObject(this.params, clazz);
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
