package com.github.skunk.data.sequence.sequence;

import com.github.skunk.data.sequence.range.SequenceRangeManager;

/**
 * 序列号区间生成器接口
 *
 *
 * @author nanfeng
 * @date 2019年12月7日
 * @since 0.0.1
 */
public interface RangeSequence extends NumSequence {

    /**
     * 设置区间管理器
     *
     * @param sequenceRangeMgr 区间管理器
     */
    void setSequenceRangeMgr(SequenceRangeManager sequenceRangeMgr);

    /**
     * 设置获取序列号名称
     *
     * @param name 名称
     */
    void setName(String name);
}
