package com.github.skunk.core;

import lombok.Getter;

/**
 * @author walker
 */
@Getter
public enum DataStatus {

    DISABLE("DISABLE", "禁用"),
    ENABLE("ENABLE", "启用");

    private String code;
    private String remarks;

    DataStatus(String code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }
}
