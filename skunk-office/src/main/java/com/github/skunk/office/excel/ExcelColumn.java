package com.github.skunk.office.excel;

import lombok.Getter;
import lombok.Setter;

/**
 * Excel 列定义
 *
 * @author nanfeng
 * @date 2019年12月29日
 * @since 0.0.1
 */
@Setter
@Getter
public class ExcelColumn {

    public static final int DATE_6 = 6;
    public static final int DATE_8 = 8;
    public static final int DATE_16 = 16;

    private String fieldName;
    private Integer fieldType;
    private String fieldDispName;

    public ExcelColumn() {
    }

    public ExcelColumn(String fieldName, String fieldDispName) {
        this.fieldName = fieldName;
        this.fieldDispName = fieldDispName;
    }

    public ExcelColumn(String fieldName, String fieldDispName, int type) {
        this.fieldName = fieldName;
        this.fieldDispName = fieldDispName;
    }
}