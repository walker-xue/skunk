package com.skunk.office.excel;

/**
 * Excel 处理异常
 *
 *
 * @author nanfeng
 * @date 2019年12月7日
 * @since 0.0.1
 */
public class ExcelHandleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExcelHandleException() {
    }

    public ExcelHandleException(String message) {
        super(message);
    }

    public ExcelHandleException(Throwable cause) {
        super(cause);
    }

    public ExcelHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelHandleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
