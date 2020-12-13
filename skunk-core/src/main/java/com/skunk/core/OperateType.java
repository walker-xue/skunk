package com.skunk.core;

import lombok.Getter;

@Getter
public enum OperateType {

    SUBMIT("submit", "提交"),
    SAVE("save", "保存");

    OperateType(String code, String remarks) {
    }
}
