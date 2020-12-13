package com.skunk.core;

/**
 * Token JWT 接口定义
 *
 * @author yanjun.xue
 * @since 2019年6月27日
 */
public interface UserJwt {

    /**
     * 获取用户 Code
     *
     * @return
     */
    String getUserCode();

    /**
     * 获取 Notes ID
     *
     * @return
     */
    String getNotesId();

    /**
     * 获取 Notes 姓名
     *
     * @return
     */
    String getNotesName();
}
