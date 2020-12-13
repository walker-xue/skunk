package com.skunk.core;

import com.skunk.core.http.HttpStatus;
import com.skunk.core.validation.Validate;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * 通用接口响应数据包，包含响应code、响应的数据包data、消息message说明
 *
 * @param <T>
 * @author walker
 * @date 2019年10月5日
 */
@SuppressWarnings("serial")
@Setter
@Getter
public class BaseResponse<T> extends BaseEntity {

    public String message;
    public int code = 200;
    private Timestamp timestamp;
    private T data;

    public BaseResponse() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    private BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    @Deprecated
    public static <T> BaseResponse<T> success() {
        return success(null);

    }

    public static <T> BaseResponse<T> OK() {
        return OK(null, null);
    }

    @Deprecated
    public static <T> BaseResponse<T> success(T data) {
        return OK(data, null);
    }

    public static <T> BaseResponse<T> OK(T data) {
        return OK(data, null);
    }

    @Deprecated
    public static <T> BaseResponse<T> success(T data, String message) {
        return OK(data, message);
    }

    public static <T> BaseResponse<T> ERROR() {
        return error(HttpStatus.BAD_REQUEST.value(), null, null);
    }

    public static <T> BaseResponse<T> ERROR(String message) {
        return error(HttpStatus.BAD_REQUEST.value(), null, message);
    }

    public static <T> BaseResponse<T> ERROR(HttpStatus httpStatus, T data, String message) {
        return error(httpStatus.value(), data, message);
    }

    public static <T> BaseResponse<T> INTERNAL_ERROR(String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, message);
    }

    public static <T> BaseResponse<T> INTERNAL_ERROR(T data, String message) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), data, message);
    }

    @Deprecated
    public static <T> BaseResponse<T> fail() {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null);
    }

    @Deprecated
    public static <T> BaseResponse<T> fail(String message) {
        return error(500, null, message);
    }

    @Deprecated
    public static <T> BaseResponse<T> fail(T data) {
        return error(500, data, null);
    }

    @Deprecated
    public static <T> BaseResponse<T> fail(int code, T data) {
        return error(code, data, null);
    }

    @Deprecated
    public static <T> BaseResponse<T> fail(int code, String message) {
        return error(code, null, message);
    }

    public static <T> BaseResponse<T> OK(T data, String message) {
        return new BaseResponse<T>(HttpStatus.OK.value(), data, message);
    }

    public static <T> BaseResponse<T> error(int code, T data, String message) {

        Validate.notNull(code);

        return new BaseResponse<T>(code, data, message);
    }
}
