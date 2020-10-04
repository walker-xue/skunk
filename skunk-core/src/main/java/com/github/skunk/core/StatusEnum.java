package com.github.skunk.core;

import lombok.Getter;

/**
 * @author walker
 */
@Getter
public enum StatusEnum {

    DISABLE("disable", "禁用"),
    ENABLE("enable", "启用");

    private String code;
    private String remarks;

    StatusEnum(String code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }
}
