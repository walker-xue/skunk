package com.github.skunk.core;

import lombok.Getter;

/**
 * 操作定义
 *
 * @author walker
 */
@Getter
public enum OperateEnum {

    SUBMIT("submit", "提交"),
    SAVE("save", "保存");

    private String code;
    private String remarks;

    OperateEnum(String code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }
}
