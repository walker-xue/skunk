package com.skunk.code.generate.config.converts;

import com.skunk.code.generate.config.ColumnType;
import com.skunk.code.generate.config.ITypeConvert;

/**
 * author 张新林
 * 时间 2019/6/12 0:07
 * 描述
 */
public class PostgreSqlTypeConvert implements ITypeConvert {

    public PostgreSqlTypeConvert() {
    }

    public ColumnType processTypeConvert(String fieldType) {
        String t = fieldType.toLowerCase();
        if (!t.contains("char") && !t.contains("text")) {
            if (t.contains("bigint")) {
                return ColumnType.LONG;
            } else if (t.contains("int")) {
                return ColumnType.INTEGER;
            } else if (!t.contains("date") && !t.contains("time") && !t.contains("year")) {
                if (t.contains("text")) {
                    return ColumnType.STRING;
                } else if (t.contains("bit")) {
                    return ColumnType.BOOLEAN;
                } else if (t.contains("decimal")) {
                    return ColumnType.BIG_DECIMAL;
                } else if (t.contains("clob")) {
                    return ColumnType.CLOB;
                } else if (t.contains("blob")) {
                    return ColumnType.BYTE_ARRAY;
                } else if (t.contains("float")) {
                    return ColumnType.FLOAT;
                } else if (t.contains("double")) {
                    return ColumnType.DOUBLE;
                } else if (!t.contains("json") && !t.contains("enum")) {
                    return t.contains("boolean") ? ColumnType.BOOLEAN : ColumnType.STRING;
                } else {
                    return ColumnType.STRING;
                }
            } else {
                return ColumnType.DATE;
            }
        } else {
            return ColumnType.STRING;
        }
    }
}
