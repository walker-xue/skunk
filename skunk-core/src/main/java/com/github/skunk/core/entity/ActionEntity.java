package com.github.skunk.core.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.skunk.core.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 实体类基础操作属性封装
 *
 * @author nanfeng
 * @date 2019年12月7日
 * @since 0.0.1
 */
@Setter
@Getter
public abstract class ActionEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String createUserId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    private String lastUpdateUserId;
}
