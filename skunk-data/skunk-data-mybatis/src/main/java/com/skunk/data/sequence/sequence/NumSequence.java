package com.skunk.data.sequence.sequence;

import com.skunk.data.sequence.exception.SequenceException;

/**
 * 序列号生成器接口
 *
 * @author nanfeng
 * @date 2019年12月7日
 * @since 0.0.1
 */
public interface NumSequence {

    /**
     * 生成下一个序列号
     *
     * @return 序列号
     * @throws SequenceException
     *     序列号异常
     */
    long nextValue() throws SequenceException;
}
