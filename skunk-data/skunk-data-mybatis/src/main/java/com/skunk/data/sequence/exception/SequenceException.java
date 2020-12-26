package com.skunk.data.sequence.exception;

import com.skunk.core.exception.BaseException;

/**
 * 序列号生成异常
 *
 * @author nanfeng
 * @date 2019年12月7日
 */
public class SequenceException extends BaseException {

    private static final long serialVersionUID = 1L;

    public SequenceException(String message) {
        super(message);
    }

    public SequenceException(Throwable cause) {
        super(cause);
    }

    public Integer getCode() {
        return 500;
    }
}
