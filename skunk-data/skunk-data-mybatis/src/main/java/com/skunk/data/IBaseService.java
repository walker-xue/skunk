package com.skunk.data;

import java.util.List;

import com.skunk.core.exception.UnrealizedException;
import com.skunk.core.filter.ListFilter;
import com.skunk.core.filter.PageFilter;
import com.skunk.core.filter.PageResult;

import tk.mybatis.mapper.entity.Example;

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
     * @return 返回更新数据条数
     */
    int insert(T entity);

    /**
     * 新增 Entit 到DB，不会替换数据默认值
     *
     * @param entity
     * @return 返回更新数据条数
     */
    int insertSelective(T entity);

    /**
     * 批量插入多个PO,主键生成方式是数据库自增方式
     *
     * @param entitys
     * @return 返回更新数据条数
     */
    int insertList(List<T> entitys);

    /**
     * 批量插入多个PO,但是数据主键需要自己提供
     *
     * @param entitys
     * @return 返回更新数据条数
     */
    int batchInsertList(List<T> entitys);

    /**
     * 通过主键物理删除
     *
     * @param id
     * @return 返回更新数据条数
     */
    int deleteByKey(Object id);

    /**
     * 通过主键让资源失效，需要子类实现
     *
     * @param id
     * @return 返回更新数据条数
     */
    default int invalidByKey(Object id) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * 通过多个主键删除PO
     *
     * @param ids
     * @return 返回更新数据条数
     */
    int deleteByKeys(List<?> ids);

    /**
     * 通过主键更新有值的字段
     *
     * @param entity
     * @return 返回更新数据条数
     */
    int updateByKeySelective(T entity);

    /**
     * 通过主键更新所有字段
     *
     * @param entity
     * @return 返回更新数据条数
     */
    int updateByKey(T entity);

    /**
     * 通过多个主键获取PO
     *
     * @param ids
     * @return 返回查询数据
     */
    List<T> selectByKeys(List<String> ids);

    /**
     * 通过主键获取PO
     *
     * @param id
     * @return　返回查询数据
     */
    T selectByKey(Object id);

    /**
     * @param entity
     * @return　返回查询数据
     */
    T selectOne(T entity);

    /**
     * @param entity
     * @return　返回查询数据
     */
    List<T> selectList(T entity);

    /**
     * @return　返回查询数据
     */
    List<T> selectListByExample(Example example);

    /**
     * @return　返回查询数据
     */
    List<T> selectAllList();

    /**
     * @param listFilter
     * @return　返回查询数据
     */
    List<T> selectList(ListFilter listFilter);

    /**
     * @param operatorId
     * @param listFilter
     * @return　返回查询数据
     */
    default List<T> selectList(final String operatorId, ListFilter listFilter) {
        throw new UnrealizedException("功能未实现");
    }

    /**
     * 资源分页查找
     *
     * @param pageFilter
     * @return　返回查询数据
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
     * @return　返回查询数据
     */
    PageResult<T> selectPage(final String operatorId, PageFilter pageFilter);

}
