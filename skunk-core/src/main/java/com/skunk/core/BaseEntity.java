package com.skunk.core;

import com.skunk.core.utils.JSONUtils;

import java.io.Serializable;


/**
 * 基础实体类
 * <p>
 * 实现了Serializable接口，并且重写了ToString方法
 *
 * @author walker
 * @date 2019年10月5日
 */
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return JSONUtils.toJson(this);
    }
}
