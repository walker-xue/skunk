package com.skunk.core.exception;

import lombok.Getter;

/**
 * 用户未找到异常
 *
 * @author nanfeng
 * @date 2019年12月10日
 * @since 0.0.1
 */
public class UserNotFoundException extends NotFoundResourceException {

	private static final long serialVersionUID = 1L;

	/**
	 * 异常对应的返回码
	 */
	@Getter
	private Integer code = Integer.valueOf(404);

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message) {
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
