package com.skunk.core.entity;

import java.util.Date;

import com.skunk.core.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 实体类基础操作属性封装
 *
 * @author walker
 * @date 2019年12月7日
 * @since 0.0.1
 */
@Setter
@Getter
public abstract class ActionEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Date createTime;

    private String createUserId;

    private Date lastUpdateTime;

    private String lastUpdateUserId;
}
