package com.skunk.data;

import java.util.List;

import com.skunk.core.exception.UnrealizedException;
import com.skunk.core.filter.ListFilter;
import com.skunk.core.filter.PageFilter;
import com.skunk.core.filter.PageResult;

/**
 * BaseService 通用接口定义
 *
 * @param <T>
 * @author nanfeng
 * @date 2019年12月7日
 * @since 0.0.1
 */
public interface IBaseService<T> {

    /**
     * 新增 Entit 到DB，会替换数据默认值
     *
     * @param entity
     * @return
     */
    default int insert(T entity) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * 批量插入多个PO
     *
     * @param entitys
     * @return
     */
    default int insertList(List<T> entitys) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * 通过主键物理删除
     *
     * @param id
     * @return
     */
    default int deleteByKey(Object id) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * 通过主键让资源失效，需要子类实现
     *
     * @param id
     * @return
     */
    default int invalidByKey(Object id) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * 通过多个主键删除PO
     *
     * @param ids
     * @return
     */
    default int deleteByKeys(List<?> ids) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * 通过主键更新所有字段
     *
     * @param entity
     * @return
     */
    default int updateByKey(T entity) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * 通过多个主键获取PO
     *
     * @param ids
     * @return
     */
    default List<T> selectByKeys(List<String> ids) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * 通过主键获取PO
     *
     * @param id
     * @return
     */
    default T selectByKey(Object id) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * @return
     */
    default List<T> selectAllList() {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * @param listFilter
     * @return
     */
    default List<T> selectList(ListFilter listFilter) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * @param operatorId
     * @param listFilter
     * @return
     */
    default List<T> selectList(final String operatorId, ListFilter listFilter) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * 资源分页查找
     *
     * @param pageFilter
     * @return
     */
    default PageResult<T> selectPage(PageFilter pageFilter) {
        return selectPage(null, pageFilter);
    }

    /**
     * 资源分页查找
     *
     * @param operatorId
     *     用户Id
     * @param pageFilter
     *     分页过滤参数
     * @return
     */
    default PageResult<T> selectPage(final String operatorId, PageFilter pageFilter) {
        throw new UnrealizedException("功能未实现");
    }
}
