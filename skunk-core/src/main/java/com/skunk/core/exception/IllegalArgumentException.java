package com.skunk.core.exception;

import lombok.Getter;

/**
 * 非法参数异常
 * <p>
 * 建议Web层抛出400错误，只有具体的异常在body中重新定义对应业务code异常
 *
 * @author nanfeng
 * @date 2019年12月10日
 * @since 0.0.1
 */
public class IllegalArgumentException extends BaseException {

    private static final long serialVersionUID = -1276435753880524349L;

    /**
     * 异常对应的返回码
     */
    @Getter
    private final Integer code = Integer.valueOf(400);

    public IllegalArgumentException() {
        super();
    }

    /**
     * @param message
     *     异常ｍｅｓｓａｇｅ
     */
    public IllegalArgumentException(String message) {
        super(message);
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
