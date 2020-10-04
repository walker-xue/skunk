package com.github.skunk.poi.file;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

import com.github.skunk.core.BaseEntity;

@SuppressWarnings("rawtypes")
@Getter
@Setter
public class ExcelHead extends BaseEntity {

    private static final long serialVersionUID = 3546571360899067511L;

    private List<ExcelColumn> columns;
    private Map<String, Map> columnsConvertMap;
    private int rowCount;
    private int columnCount;
}