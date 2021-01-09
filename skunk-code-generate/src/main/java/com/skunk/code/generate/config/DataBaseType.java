package com.skunk.code.generate.config;

/**
 * author 张新林
 * 时间 2019/6/11 23:57
 * 描述
 */
public enum DataBaseType {
    MYSQL("mysql"),
    ORACLE("oracle"),
    SQL_SERVER("sql_server"),
    POSTGRE_SQL("postgre_sql");

    private final String value;

    private DataBaseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
