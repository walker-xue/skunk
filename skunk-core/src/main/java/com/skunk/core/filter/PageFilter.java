package com.skunk.core.filter;

/**
 * 分页接口定义
 *
 * @author walkerＳ
 * @since 2019年5月13日
 */
public interface PageFilter extends ListFilter {

    // 显示行数
    String PAGE_SIZE = "pageSize";

    // 第几页
    String PAGE_NO = "pageNo";

    /**
     * 默认显示行
     */
    int DEFAULT_PAGE_SIZE = 10;

    /**
     * 默认显示页
     */
    int DEFAULT_PAGE_INDEX = 1;

    /**
     * 获取当前页
     *
     * @return
     */
    int getPageNo();

    /**
     * 设置是否查询总数量
     *
     * @param notQueryTotalNum
     */
    void setNotQueryTotalNum(Boolean notQueryTotalNum);

    /**
     * 设置是否查询总数量
     */
    boolean isQueryTotalNum();

    /**
     * 获取每页中多少条
     *
     * @return
     */
    int getPageSize();

    /**
     * 获取排序字符串
     *
     * @return
     */
    String getOrderBy();

    /**
     * 获取List查询参数
     *
     * @return
     */
    ListFilter listFilter();
}
