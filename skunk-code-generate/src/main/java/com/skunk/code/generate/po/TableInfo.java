package com.skunk.code.generate.po;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 数据表entity
 *
 * @author walker
 */
@Setter
@Getter
public class TableInfo {

    private String name;
    private String comment = "";
    private List<TableField> fields;

    public TableInfo() {
    }
}
