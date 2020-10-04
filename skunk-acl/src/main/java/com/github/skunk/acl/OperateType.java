package com.github.skunk.acl;

/**
 * 操作类型定义
 */
public enum OperateType {

    ADD(1),
    MODIFY(2),
    DELETE(3),
    DELETES(4);

    int value;

    OperateType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
