package com.github.skunk.core;

public enum WhetherType {

    YES("YES", "提交"),
    NO("NO", "保存");

    private String code;
    private String remarks;

    WhetherType(String code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }
}
