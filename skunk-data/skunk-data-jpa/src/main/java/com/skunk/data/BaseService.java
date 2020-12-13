package com.skunk.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.skunk.core.utils.ReflectionUtils;

import lombok.extern.slf4j.Slf4j;

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

    protected BaseService() {
        entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
    }

}
