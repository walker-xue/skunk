package com.skunk.data.sequence.range;

import com.skunk.data.sequence.exception.SequenceException;

/**
 * 区间管理器
 *
 * @author nanfeng
 * @date 2019年12月7日
 * @since 0.0.1
 */
public interface SequenceRangeManager {

    /**
     * 获取指定区间名的下一个区间
     *
     * @param name
     *     区间名
     * @return 返回区间
     * @throws SequenceException
     *     异常
     */
    SequenceRange nextRange(String name) throws SequenceException;

    /**
     * 初始化
     */
    void init();
}
