package com.skunk.data.sequence.range.db;

import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skunk.data.sequence.exception.SequenceException;
import com.skunk.data.sequence.range.SequenceRange;
import com.skunk.data.sequence.range.SequenceRangeManager;

/**
 * DB区间管理器
 *
 * @author nanfeng
 * @date 2019年12月7日
 * @since 0.0.1
 */
@Component
public class DbSequenceRangeManager implements SequenceRangeManager {

    @Autowired
    DataSource dataSource;

    public DbSequenceRangeManager(DataSource dataSource) {
        if (!Objects.isNull(dataSource)) {
            this.dataSource = dataSource;
        }
    }
    /**
     * 表名前缀，为防止数据库表名冲突，默认带上这个前缀
     */
    private final static String TABLENAME_PREFIX = "x_sequence_";

    /**
     * 区间步长
     */
    private int step = 1000;
    /**
     * 区间起始位置，真实从stepStart+1开始
     */
    private long stepStart = 0;
    /**
     * 获取区间失败重试次数
     */
    private int retryTimes = 100;
    /**
     * 表名，默认range
     */
    private String tableName = "range";

    @Override
    public SequenceRange nextRange(String name) throws SequenceException {
        if (isEmpty(name)) {
            throw new SecurityException("[DbSeqRangeMgr-nextRange] name is empty.");
        }

        Long oldValue;
        Long newValue;

        for (int i = 0; i < getRetryTimes(); i++) {
            oldValue = DbHelper.selectRange(dataSource, getRealTableName(), name, getStepStart());

            if (null == oldValue) {
                //区间不存在，重试
                continue;
            }

            newValue = oldValue + getStep();

            if (DbHelper.updateRange(dataSource, getRealTableName(), newValue, oldValue, name)) {
                return new SequenceRange(oldValue + 1, newValue);
            }
            //else 失败重试
        }

        throw new SequenceException("Retried too many times, retryTimes = " + getRetryTimes());
    }

    @Override
    public void init() {
        checkParam();
        DbHelper.createTable(dataSource, getRealTableName());
    }

    private boolean isEmpty(String str) {
        return null == str || str.trim().length() == 0;
    }

    private String getRealTableName() {
        return TABLENAME_PREFIX + getTableName();
    }

    private void checkParam() {
        if (step <= 0) {
            throw new SecurityException("[DbSeqRangeMgr-checkParam] step must greater than 0.");
        }
        if (stepStart < 0) {
            throw new SecurityException("[DbSeqRangeMgr-setStepStart] stepStart < 0.");
        }
        if (retryTimes <= 0) {
            throw new SecurityException("[DbSeqRangeMgr-setRetryTimes] retryTimes must greater than 0.");
        }
        if (null == dataSource) {
            throw new SecurityException("[DbSeqRangeMgr-setDataSource] dataSource is null.");
        }
        if (isEmpty(tableName)) {
            throw new SecurityException("[DbSeqRangeMgr-setTableName] tableName is empty.");
        }
    }

    ////////getter and setter

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public long getStepStart() {
        return stepStart;
    }

    public void setStepStart(long stepStart) {
        this.stepStart = stepStart;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
