package com.skunk.core;

import lombok.Getter;

@Getter
public enum OperateType {

    SUBMIT("SUBMIT", "提交"),
    SAVE("SAVE", "保存");

    private String code;
    private String remarks;

    OperateType(String code, String remarks) {
    }
}
