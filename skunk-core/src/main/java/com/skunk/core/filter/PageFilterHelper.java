package com.skunk.core.filter;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 分页插件实现
 *
 * @author walker
 * @since 2019年5月13日
 */
@Getter
@Setter
public class PageFilterHelper extends ListFilterHelper implements PageFilter {

    /* 当前页 */
    private int pageNo;

    /* 每页显示条数 */
    private int pageSize;

    /* 是否计算总页数 */
    private boolean isQueryTotal;


    PageFilterHelper(SortOrder.Type orderMethod, String orderField, boolean isOrderBy, Map<String, Object> params) {
        super(orderMethod, orderField, isOrderBy, params);
    }


    private PageFilterHelper(Builder builder) {
        super();
        this.pageNo = builder.pageNo;
        this.pageSize = builder.pageSize;
        this.isQueryTotal = builder.isQueryTotal;
        this.params = builder.params;
        this.orderField = builder.orderField;
        this.orderMethod = builder.orderMethod;
    }

    public static class Builder {

        private int pageNo;
        private int pageSize;
        private boolean isQueryTotal;
        private Map<String, Object> params;
        private String orderField;
        private SortOrder.Type orderMethod;


        public Builder pageNo(int pageNo) {
            this.pageNo = pageNo;
            return this;
        }

        public Builder pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder type(boolean isQueryTotal) {
            this.isQueryTotal = isQueryTotal;
            return this;
        }

        public Builder params(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder orderField(String orderField) {
            this.orderField = orderField;
            return this;
        }

        public Builder orderMethod(SortOrder.Type orderMethod) {
            this.orderMethod = orderMethod;
            return this;
        }


        public PageFilterHelper build() {
            return new PageFilterHelper(this);
        }
    }


    @Override
    public int getPageNo() {
        return pageNo;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }


    @Override
    public ListFilter listFilter() {

        return ListFilterHelper.builder()
            .params(getParams())
            .orderField(orderField)
            .orderMethod(orderMethod)
            .build();
    }


    @Override
    public void setIsQueryTotal(boolean notQueryTotalNum) {
        this.isQueryTotal = notQueryTotalNum;
    }

    @Override
    public boolean isQueryTotal() {
        return !isQueryTotal;
    }

}
