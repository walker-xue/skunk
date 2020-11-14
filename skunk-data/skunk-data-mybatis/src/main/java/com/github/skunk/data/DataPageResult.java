package com.github.skunk.data;

import java.util.Collections;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.skunk.core.filter.PageResult;

public class DataPageResult<E> extends PageResult {

    /**
     * @param list
     */
    public DataPageResult(List<E> list) {
        if (list instanceof Page) {
            Page page = (Page) list;
            this.pageNo = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.totalCount = page.getTotal();
        } else {
            this.totalCount = list.size();
        }
        this.list = list;
        this.footer = Collections.emptyList();
    }

    /**
     * @param total
     * @param list
     */
    public DataPageResult(long total, List<E> list) {
        super(total, list);
        if (list instanceof Page) {
            Page page = (Page) list;
            this.pageNo = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.totalCount = page.getTotal();
        } else {
            this.totalCount = total;
        }
        this.list = list;
        this.footer = Collections.emptyList();
    }
}
