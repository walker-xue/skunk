package com.skunk.data.sequence;

import com.skunk.data.sequence.sequence.NumSequence;

/**
 * 序列号生成器构建者
 *
 * @author nanfeng
 * @date 2019年12月7日
 * @since 0.0.1
 */
public interface SequenceBuilder {

    /**
     * 构建一个序列号生成器
     *
     * @return 序列号生成器
     */
    NumSequence build();
}
