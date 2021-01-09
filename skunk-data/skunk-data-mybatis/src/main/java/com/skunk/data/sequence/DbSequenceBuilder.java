package com.skunk.data.sequence;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import com.skunk.data.sequence.range.db.DbSequenceRangeManager;
import com.skunk.data.sequence.sequence.NumSequence;
import com.skunk.data.sequence.sequence.impl.DefaultRangeSequence;

/**
 * 基于DB取步长，序列号生成器构建者
 *
 * @author nanfeng
 * @date 2019年12月7日
 * @since 0.0.1
 */
@Service
public class DbSequenceBuilder implements SequenceBuilder {

    /**
     * 数据库数据源[必选]
     */
    private DataSource dataSource;

    /**
     * 业务名称[必选]
     */
    private String bizName;
    /**
     * 存放序列号步长的表[可选：默认：sequence]
     */
    private String tableName = "sequence";
    /**
     * 并发是数据使用了乐观策略，这个是失败重试的次数[可选：默认：100]
     */
    private int retryTimes = 100;
    /**
     * 获取range步长[可选：默认：1000]
     */
    private int step = 1000;

    /**
     * 序列号分配起始值[可选：默认：0]
     */
    private long stepStart = 0;
    public static DbSequenceBuilder create() {
        DbSequenceBuilder builder = new DbSequenceBuilder();
        return builder;
    }
    @Override
    public NumSequence build() {
        //利用DB获取区间管理器
        DbSequenceRangeManager dbSeqRangeMgr = new DbSequenceRangeManager(this.dataSource);
        dbSeqRangeMgr.setTableName(this.tableName);
        dbSeqRangeMgr.setRetryTimes(this.retryTimes);
        dbSeqRangeMgr.setStep(this.step);
        dbSeqRangeMgr.setStepStart(stepStart);
        dbSeqRangeMgr.init();
        //构建序列号生成器
        DefaultRangeSequence sequence = new DefaultRangeSequence();
        sequence.setName(this.bizName);
        sequence.setSequenceRangeMgr(dbSeqRangeMgr);
        return sequence;
    }
    public DbSequenceBuilder dataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public DbSequenceBuilder tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public DbSequenceBuilder retryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public DbSequenceBuilder step(int step) {
        this.step = step;
        return this;
    }

    public DbSequenceBuilder bizName(String bizName) {
        this.bizName = bizName;
        return this;
    }

    public DbSequenceBuilder stepStart(long stepStart) {
        this.stepStart = stepStart;
        return this;
    }
}
