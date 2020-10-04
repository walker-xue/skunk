package com.github.skunk.web;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回码
 *
 * @author nanfeng
 * @date 2019年12月29日
 * @since 0.0.1
 */
public enum HttpCode {

    OK(200, "操作成功"),
    WARING_MSG(201, "提醒信息"),
    REGISTERED(203, "已注册"),
    NOT_REGISTER(204, "未注册"),
    INVALID_AUTH_CODE(205, "无效验证码"),
    DELETED(206, "已删除或下线"),
    NOT_LOGIN(210, "未登录"),
    REDIRECT(301, "session失效，重定向"),

    _4000_BAD_CREDENTIALS(400401, "用户或者密码错误"),
    USER_NAME_NOT_FOUND(404, "用户不能找到"),
    CERTIFICATION_EXPIRED(403, "认证过期"),
    @Deprecated
    INTERNAL_SERVER_ERROR(500, "服务器内部错误");

    public final int status;
    public final String message;

    HttpCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private final static Map<Integer, HttpCode> valueMap = new HashMap<>();

    static {
        for (HttpCode err : HttpCode.values()) {
            if (valueMap.put(err.status, err) != null) {
                System.out.println("Warnning! Code 重复定义的值, code=" + err.status);
            }
        }
    }

    public static HttpCode valueOf(int status) {
        HttpCode err = valueMap.get(status);
        if (err != null) {
            return err;
        }
        throw new RuntimeException("status=" + status + "的NPConfigError错误");
    }
}
