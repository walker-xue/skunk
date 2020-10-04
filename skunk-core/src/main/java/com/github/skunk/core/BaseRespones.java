package com.github.skunk.core;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

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
public class BaseRespones<T> extends BaseEntity {

    // value = "时间", position = 10)
    private Timestamp timestamp;

    // @ApiModelProperty(value = "结果数据", position = 2)
    private T data;
    //    @ApiModelProperty(value = "消息说明", position = 1)
    public String message;
    //    @ApiModelProperty(value = "响应代码", position = 0)
    public int code = 200;

    /**
     *
     */
    public BaseRespones() {
    }

    /**
     *
     * @param data
     */
    public BaseRespones(T data) {
        this.data = data;
        this.message = null;
    }

    /**
     *
     * @param <T>
     * @return
     */
    public static <T> BaseRespones<T> success() {
        return success(null);
    }

    /**
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseRespones<T> success(T data) {
        return success(data, null);
    }

    /**
     *
     * @param data
     * @param message
     * @param <T>
     * @return
     */
    public static <T> BaseRespones<T> success(T data, String message) {
        BaseRespones<T> respones = new BaseRespones<T>(data);
        respones.setMessage(message);
        respones.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return respones;
    }

    /**
     *
     * @param <T>
     * @return
     */
    public static <T> BaseRespones<T> fail() {
        return fail(500, "服务器内部错误");
    }

    /**
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> BaseRespones<T> fail(String message) {
        return fail(500, message);
    }

    /**
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseRespones<T> fail(T data) {
        return fail(500, data);
    }

    /**
     * @param code
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseRespones<T> fail(int code, T data) {
        return fail(code, data, null);
    }

    /**
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> BaseRespones<T> fail(int code, String message) {
        return fail(code, null, message);
    }

    /**
     * @param code
     * @param data
     * @param message
     * @param <T>
     * @return
     */
    public static <T> BaseRespones<T> fail(int code, T data, String message) {
        BaseRespones<T> respones = new BaseRespones<T>(data);
        respones.setMessage(message);
        respones.setCode(code);
        respones.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return respones;
    }
}
