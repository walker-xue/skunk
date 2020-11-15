package com.github.skunk.core.exception;

import lombok.Getter;

/**
 * 不能找到资源信息异常
 *
 * @author nanfeng
 * @date 2019年12月10日
 * @since 0.0.1
 */
public class NotFoundResourceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 异常对应的返回码
	 */
	@Getter
	private Integer code = Integer.valueOf(404);

	public NotFoundResourceException() {
		super();
	}

	public NotFoundResourceException(String message) {
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
