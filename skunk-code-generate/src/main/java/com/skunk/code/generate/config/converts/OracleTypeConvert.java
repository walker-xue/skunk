package com.skunk.code.generate.config.converts;

import com.skunk.code.generate.config.ColumnType;
import com.skunk.code.generate.config.ITypeConvert;

/**
 * author 张新林
 * 时间 2019/6/12 0:07
 * 描述
 */
public class OracleTypeConvert implements ITypeConvert {

    public OracleTypeConvert() {
    }

    public ColumnType processTypeConvert(String fieldType) {
        String t = fieldType.toUpperCase();
        if (t.contains("CHAR")) {
            return ColumnType.STRING;
        } else if (!t.contains("DATE") && !t.contains("TIMESTAMP")) {
            if (t.contains("NUMBER")) {
                if (t.matches("NUMBER\\(+\\d\\)")) {
                    return ColumnType.INTEGER;
                } else {
                    return t.matches("NUMBER\\(+\\d{2}+\\)") ? ColumnType.LONG : ColumnType.DOUBLE;
                }
            } else if (t.contains("FLOAT")) {
                return ColumnType.FLOAT;
            } else if (t.contains("clob")) {
                return ColumnType.CLOB;
            } else if (t.contains("BLOB")) {
                return ColumnType.OBJECT;
            } else if (t.contains("binary")) {
                return ColumnType.BYTE_ARRAY;
            } else {
                return t.contains("RAW") ? ColumnType.BYTE_ARRAY : ColumnType.STRING;
            }
        } else {
            return ColumnType.DATE;
        }
    }
}
