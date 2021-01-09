package com.skunk.core;

import lombok.Getter;

/**
 * 定义通用的操作类型
 *
 * @author walker
 */
@Getter
public enum OperateType {

    SUBMIT("SUBMIT", "提交"),
    SAVE("SAVE", "保存");

    private final String code;
    private final String remarks;

    OperateType(String code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }
}
