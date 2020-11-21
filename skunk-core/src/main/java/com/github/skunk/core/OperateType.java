package com.github.skunk.core;

import lombok.Getter;

@Getter
public enum OperateType {

    SUBMIT("submit", "提交"),
    SAVE("save", "保存");

    private String code;
    private String remarks;

    OperateType(String code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }
}
