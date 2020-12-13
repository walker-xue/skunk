package com.skunk.office.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.skunk.core.collectors.CollectionUtils;
import com.skunk.core.utils.StringUtils;
import com.skunk.office.ReflectUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * POI Excel 文件工具封装
 *
 * @author nanfeng
 * @date 2019年12月29日
 * @since 0.0.1
 */
@Slf4j
public class ExcelHelper {

    /**
     *
     * @param head
     * @param file
     * @param cls
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<?> importToObjectList(ExcelHead head, File file, Class<?> cls) {

        Objects.requireNonNull(head);
        Objects.requireNonNull(file);
        Objects.requireNonNull(cls);

        // 根据excel生成list类型的数据
        try (FileInputStream fis = new FileInputStream(file)) {
            List<List> rows = excelFileConvertToList(fis);
            // 删除头信息
            List<Object> headName = new ArrayList<>();
            for (int i = 0; i < head.getRowCount(); i++) {
                if (i == head.getRowCount() - 1) {
                    headName = rows.get(0);
                }
                rows.remove(0);
            }
            Map<Integer, String> excelHeadMap = convertExcelHeadToMap(head.getColumns(), headName);
            Map<String, Integer> filedTypeMap = convertExcelHeadToPropertyType(head.getColumns());
            return buildDataObject(excelHeadMap, filedTypeMap, rows, cls);
        } catch (FileNotFoundException ex) {
            log.error("文件读取异常:" + ex.getMessage());
            throw new ExcelHandleException("文件读取异常");
        } catch (IOException e) {
            log.error("新建Excel错误:" + e.getMessage());
            throw new ExcelHandleException("新建Excel错误");
        }
    }

    /**
     * @param head
     * @param dataList
     * @return
     */
    public static Workbook exportExcelFile(ExcelHead head, List<?> dataList) {

        Objects.requireNonNull(head);
        Objects.requireNonNull(dataList);

        Workbook wb = new SXSSFWorkbook();
        Sheet sheet = wb.createSheet();
        // 生成Excel头
        buildExcelHead(sheet, head);
        // 生成导出数据
        buildExcelData(sheet, head, dataList);
        return wb;
    }

    /**
     * @param excelColumns
     * @param headName
     * @return
     */
    private static Map<Integer, String> convertExcelHeadToMap(List<ExcelColumn> excelColumns, List<Object> headName) {

        Validate.notEmpty(excelColumns);
        Validate.notEmpty(headName);

        if (excelColumns.size() != headName.size()) {
            throw new ExcelHandleException("导入模板与资源不匹配，请核实");
        }
        Map<Integer, String> excelHeadMap = new HashMap<Integer, String>();
        for (int index = 0; index < headName.size(); index++) {
            boolean flag = false;
            String hName = (String) headName.get(index);
            for (ExcelColumn excelColumn : excelColumns) {
                if (StringUtils.filterNull(hName).equals(excelColumn.getFieldDispName().trim())) {
                    flag = true;
                    excelHeadMap.put(index, excelColumn.getFieldName());
                }
            }
            if (!flag) {
                throw new ExcelHandleException("列：\"" + hName + "\" 不是此资源中的属性");
            }
        }
        return excelHeadMap;
    }

    /**
     * @param columns
     * @return
     */
    private static Map<String, Integer> convertExcelHeadToPropertyType(List<ExcelColumn> columns) {

        Objects.requireNonNull(columns);

        Map<String, Integer> map = new HashMap<>();
        columns.stream().forEach(excelColumn -> map.put(excelColumn.getFieldName(), excelColumn.getFieldType()));
        return map;

    }

    /**
     * 构建 Sheet 中 Row 数据
     *
     * @param sheet
     * @param head
     * @param dataList
     */
    private static void buildExcelData(Sheet sheet, ExcelHead head, List<?> dataList) {

        Objects.requireNonNull(head);
        Objects.requireNonNull(dataList);

        List<ExcelColumn> excelColumns = head.getColumns();

        // 从第几行开始插入数据
        int startRow = head.getRowCount();
        int order = 1;
        for (Object obj : dataList) {
            Row row = sheet.createRow(startRow++);
            for (int j = 0; j < excelColumns.size(); j++) {
                Cell cell = row.createCell(j);
                String fieldName = excelColumns.get(j).getFieldName();
                if (fieldName != null) {

                    Object valueObject = ReflectUtils.getBeanProperty(obj, fieldName);
                    if (valueObject == null) {
                        cell.setCellValue("");
                    } else if (valueObject instanceof Integer) {
                        cell.setCellValue((Integer) valueObject);
                    } else if (valueObject instanceof Long) {
                        cell.setCellValue((Long) valueObject);
                    } else if (valueObject instanceof Double) {
                        cell.setCellValue((Double) valueObject);
                    } else if (valueObject instanceof Float) {
                        cell.setCellValue((Float) valueObject);
                    } else if (valueObject instanceof BigDecimal) {
                        cell.setCellValue(((BigDecimal) valueObject).doubleValue());
                    } else if (valueObject instanceof String) {
                        cell.setCellValue((String) valueObject);
                    } else if (valueObject instanceof Date) {
                        cell.setCellValue(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format((Date) valueObject));
                    } else {
                        cell.setCellValue(valueObject.toString());
                    }
                } else {
                    cell.setCellValue(order++);
                }
            }
        }
    }

    private static void buildExcelHead(Sheet sheet, ExcelHead head) {
        List<ExcelColumn> excelColumns = head.getColumns();
        for (int index = 0; index < head.getRowCount(); index++) {
            Row row = sheet.createRow(index);
            for (int j = 0; j < excelColumns.size(); j++) {
                Cell cell = row.createCell(j);
                String value = excelColumns.get(j).getFieldDispName();
                cell.setCellValue(value);
            }
        }
    }

    @SuppressWarnings({"rawtypes"})
    public static List<List> excelFileConvertToList(FileInputStream fis) throws IOException {

        Objects.requireNonNull(fis);

        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheetAt(0);

        List<List> rows = new ArrayList<List>();
        for (Row row : sheet) {
            List<Object> cells = new ArrayList<Object>();
            for (Cell cell : row) {
                Object obj;
                switch (cell.getCellType()) {
                    case STRING:
                        obj = cell.getRichStringCellValue().getString();
                        break;
                    case NUMERIC:
                        obj = DateUtil.isCellDateFormatted(cell) ? cell.getDateCellValue() : cell.getNumericCellValue();
                        break;
                    case BOOLEAN:
                        obj = cell.getBooleanCellValue();
                        break;
                    case FORMULA:
                        obj = cell.getNumericCellValue();
                        break;
                    default:
                        obj = null;
                }
                cells.add(obj);
            }
            rows.add(cells);
        }
        return rows;
    }

    /**
     *
     * @param excelHeadMap
     * @param filedTypeMap
     * @param rows
     * @param cls
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static List<Object> buildDataObject(Map<Integer, String> excelHeadMap, Map<String, Integer> filedTypeMap, List<List> rows, Class<?> cls) {
        List<Object> contents = new ArrayList<>();
        for (List<?> list : rows) {
            // 如果当前第一列中无数据,则忽略当前行的数据
            if (CollectionUtils.isEmpty(list) || list.get(0) == null) {
                break;
            }
            // 当前行的数据放入map中,生成<fieldName, value>的形式
            Map<String, Object> rowMap = rowListToMap(excelHeadMap, list);
            try {
                // 将当前行转换成对应的对象
                Object obj = cls.getDeclaredConstructor().newInstance();
                ReflectUtils.populateBean(obj, rowMap, filedTypeMap);
                contents.add(obj);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("类实例化异常:" + e.getMessage());
                throw new ExcelHandleException("类实例化异常");
            }
        }
        return contents;
    }

    /**
     * 将行转行成map,生成<fieldName, value>的形式
     *
     * @param headerMap 表头信息
     * @param list      数据
     * @return Map
     */
    private static Map<String, Object> rowListToMap(Map<Integer, String> headerMap, List<?> list) {
        Map<String, Object> rowMap = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            String fieldName = headerMap.get(i);
            if (StringUtils.isNotEmpty(fieldName)) {
                rowMap.put(fieldName, list.get(i));
            }
        }
        return rowMap;
    }
}