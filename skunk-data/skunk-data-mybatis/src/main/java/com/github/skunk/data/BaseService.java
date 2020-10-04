package com.github.skunk.data;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.skunk.core.collectors.CollectorUtils;
import com.github.skunk.core.filter.ListFilter;
import com.github.skunk.core.filter.PageFilter;
import com.github.skunk.core.filter.PageResult;
import com.github.skunk.core.utils.ReflectionUtils;
import com.github.skunk.data.utils.BasisMapper;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;

/**
 * 关系型数据库持久化
 *
 * @param <T>
 * @author walker
 * @since 2019年5月13日
 */
@Slf4j
public class BaseService<T> implements IBaseService<T> {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private Class<T> entityClass;

    @Autowired
    protected BasisMapper<T> mapper;

    @Autowired
    protected SqlMapper sqlMapper;

    protected BaseService() {
        entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

    @Override
    public int insert(T entity) {
        return mapper.insert(entity);
    }

    @Override
    public int insertSelective(T entity) {
        return mapper.insertSelective(entity);
    }

    @Override
    public int insertList(List<T> entitys) {
        if (CollectorUtils.isEmpty(entitys)) {
            return 0;
        }
        return mapper.insertList(entitys);
    }
    /**
     * @param entitys
     * @return
     */
    @Override
    public int batchInsertList(List<T> entitys) {
        if (CollectorUtils.isEmpty(entitys)) {
            return 0;
        }
        return mapper.batchInsertList(entitys);
    }

    @Override
    public int deleteByKey(Object id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteByKeys(List<?> ids) {
        if (CollectorUtils.isEmpty(ids)) {
            return 0;
        }
        String joinId = ids.stream().map(id -> id.toString()).collect(Collectors.joining(","));
        log.debug("delete resouce ids:{}", joinId);
        return mapper.deleteByIds(joinId);
    }

    @Override
    public int updateByKeySelective(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int updateByKey(T entity) {
        return mapper.updateByPrimaryKey(entity);
    }

    @Override
    public T selectOne(T entity) {
        return mapper.selectOne(entity);
    }

    @Override
    public List<T> selectList(T entity) {
        return mapper.select(entity);
    }

    @Override
    public List<T> selectAllList() {
        return mapper.selectAll();
    }

    /**
     * 通过Example查询数据
     *
     * @param example
     * @return
     */
    @Override
    public List<T> selectListByExample(Example example) {
        return mapper.selectByExample(example);
    }

    /**
     * 通过主键获取实例
     *
     * @param id
     * @return 返回实例
     */
    @Override
    public T selectByKey(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 通过多个逐渐返回多个实例
     *
     * @param ids
     * @return 返回多个实例
     */
    @Override
    public List<T> selectByKeys(List<String> ids) {
        return mapper.selectByIds(CollectorUtils.join(ids, ","));
    }

    /**
     * 通过查询条件查询数据，并且以数组的方式返回数据
     *
     * @param listFilter
     * @return 返回List数据
     */
    @Override
    public List<T> selectList(ListFilter listFilter) {
        Weekend<T> weekend = new Weekend<>(this.entityClass);
        if (CollectorUtils.isNotEmpty(listFilter.getParams())) {

        }
        if (StringUtils.isNoneBlank(listFilter.getOrderBy()) && listFilter.isOrderBy()) {
            weekend.setOrderByClause(listFilter.getOrderBy());
        }
        return mapper.selectByExample(weekend);
    }

    /**
     * 通过分页的形式查询数据
     *
     * @param operatorId
     *     用户Id
     * @param pageFilter
     *     分页过滤参数
     * @return 返回查询结果
     */
    @Override
    public PageResult<T> selectPage(String operatorId, PageFilter pageFilter) {
        PageHelper.startPage(pageFilter.getPageNo(), pageFilter.getPageSize(), pageFilter.getOrderBy());

        PageInfo<T> tPageInfo = new PageInfo<>(selectList(operatorId, pageFilter.listFilter()));
        return new PageResult<T>(tPageInfo.getTotal(), tPageInfo.getList(), pageFilter);
    }
}
