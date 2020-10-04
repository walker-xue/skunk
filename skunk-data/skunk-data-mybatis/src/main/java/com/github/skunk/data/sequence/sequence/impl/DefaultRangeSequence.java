package com.github.skunk.data.sequence.sequence.impl;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.github.skunk.data.sequence.exception.SequenceException;
import com.github.skunk.data.sequence.range.SequenceRange;
import com.github.skunk.data.sequence.range.SequenceRangeManager;
import com.github.skunk.data.sequence.sequence.RangeSequence;

/**
 * 序列号区间生成器接口默认实现
 *
 * @author nanfeng
 * @date 2019年12月7日
 * @since 0.0.1
 */
public class DefaultRangeSequence implements RangeSequence {

    /**
     * 获取区间是加一把独占锁防止资源冲突
     */
    private final Lock lock = new ReentrantLock();

    /**
     * 序列号区间管理器
     */
    private SequenceRangeManager sequenceRangeMgr;

    /**
     * 当前序列号区间
     */
    private volatile SequenceRange currentRange;

    /**
     * 需要获取区间的业务名称
     */
    private String name;

    @Override
    public long nextValue() throws SequenceException {
        //当前区间不存在，重新获取一个区间
        if (null == currentRange) {
            lock.lock();
            try {
                if (null == currentRange) {
                    currentRange = sequenceRangeMgr.nextRange(name);
                }
            } finally {
                lock.unlock();
            }
        }

        //当value值为-1时，表明区间的序列号已经分配完，需要重新获取区间
        long value = currentRange.getAndIncrement();
        if (value == -1) {
            lock.lock();
            try {
                for (; ; ) {
                    if (currentRange.isOver()) {
                        currentRange = sequenceRangeMgr.nextRange(name);
                    }

                    value = currentRange.getAndIncrement();
                    if (value == -1) {
                        continue;
                    }

                    break;
                }
            } finally {
                lock.unlock();
            }
        }

        if (value < 0) {
            throw new SequenceException("Sequence value overflow, value = " + value);
        }

        return value;
    }

    @Override
    public void setSequenceRangeMgr(SequenceRangeManager sequenceRangeMgr) {
        this.sequenceRangeMgr = sequenceRangeMgr;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

}
