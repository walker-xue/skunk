package com.github.skunk.data;

import java.util.Collections;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.skunk.core.filter.PageResult;

public class PageHelperResult<E> extends PageResult {

    public PageHelperResult(List<E> list) {
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

    public static <E> PageHelperResult<E> build(List<E> list) {
        return new PageHelperResult(list);
    }

    public PageHelperResult(long total, List<E> list) {
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

    public static <E> PageHelperResult<E> build(long total, List<E> list) {
        return new PageHelperResult(total, list);
    }
}
