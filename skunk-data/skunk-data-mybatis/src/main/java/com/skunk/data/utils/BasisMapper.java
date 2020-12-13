package com.skunk.data.utils;

import com.skunk.data.mapper.special.BatchInsertListMapper;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 通用Mapper
 *
 * @author nanfeng
 * @date 2019年12月7日
 * @since 0.0.1
 */
public interface BasisMapper<T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T>, BatchInsertListMapper<T> {

}