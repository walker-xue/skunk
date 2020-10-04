package com.github.skunk.core.filter;

import java.util.Collections;
import java.util.List;

import com.github.skunk.core.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 定义分页结果集
 * <p>
 * 以分页的形式返回查询结果
 *
 * @param <E>
 * @author walker
 * @since 2019年5月13日
 */
@SuppressWarnings("serial")
@Getter
@Setter
public class PageResult<E> extends BaseEntity {

    /**
     * 总记录数
     */
    public final long totalCount;
    /**
     * 本次返回的数据
     */
    public final List<E> list;
    /**
     * 页脚数据集
     */
    private final List<?> footer;
    /**
     * 页号
     */
    public int pageNo;
    /**
     * 每页数据大小
     */
    public int pageSize;

    public PageResult() {
        this.totalCount = 0;
        this.list = Collections.emptyList();
        this.footer = Collections.emptyList();
    }

    public PageResult(long total, List<E> list) {
        this.totalCount = total;
        this.list = list;
        this.footer = Collections.emptyList();
    }

    public PageResult(long total, List<E> list, PageFilter pageFilter) {
        this.totalCount = total;
        this.list = list;
        this.footer = Collections.emptyList();
        this.pageNo = pageFilter.getPageNo();
        this.pageSize = pageFilter.getPageSize();
    }

    /**
     * @param total
     * @param list
     * @param footer
     */
    public PageResult(long total, List<E> list, List<?> footer) {
        this.totalCount = total;
        this.list = list;
        this.footer = footer;
    }
}
