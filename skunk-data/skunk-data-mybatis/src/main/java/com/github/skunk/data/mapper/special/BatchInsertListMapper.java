package com.github.skunk.data.mapper.special;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import com.github.skunk.data.mapper.provider.BatchInsertSpecialProvider;

/**
 * 通用Mapper接口,特殊方法，批量插入，支持批量插入的数据库都可以使用，例如mysql,h2等
 * <p>
 * 另外数据主键，需要自己提供
 *
 * @param <T>
 *     不能为空
 * @author liuzh
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface BatchInsertListMapper<T> {

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，另外该接口实体包含`id`属性并且必须为自己提供主键
     *
     * @param recordList
     * @return
     */
    @Options(useGeneratedKeys = false)
    @InsertProvider(type = BatchInsertSpecialProvider.class, method = "dynamicSQL")
    int batchInsertList(List<? extends T> recordList);

}