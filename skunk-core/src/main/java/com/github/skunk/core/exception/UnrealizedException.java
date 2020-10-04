package com.github.skunk.core.exception;

import lombok.Getter;

/**
 * 未实现异常
 *
 * @author nanfeng
 * @date 2019年12月10日
 * @since 0.0.1
 */
public class UnrealizedException extends RuntimeException {

    private static final long serialVersionUID = -8101702898478323573L;

    /**
     * 异常对应的返回码
     */
    @Getter
    private Integer code = Integer.valueOf(400);

    public UnrealizedException() {
        super();
    }

    public UnrealizedException(String message) {
        super(message);
    }

    protected UnrealizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 不输出异常堆栈信息
     *
     * @return
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
