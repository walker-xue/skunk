package com.skunk.core.filter;

import com.skunk.core.validation.Validate;

import javax.validation.constraints.NotBlank;


/**
 * 排序结构体定义
 *
 * @author walker
 */
public class SortOrder {

    /**
     * 排序字段
     */
    public static final String ORDER_BY_KEY = "orderField";

    /**
     * 排序方式
     */
    public static final String ORDER_METHOD_KEY = "orderMethod";

    public enum Type {

        DESC("desc"),
        ASC("asc");

        private String sort;

        Type(String sort) {
            this.sort = sort;
        }

        public static Type valueCode(@NotBlank String mode) {

            Validate.notBlank(mode);

            switch (mode.toLowerCase()) {
                case "asc":
                    return ASC;
                default:
                    return DESC;
            }

        }

        public String getSort() {
            return this.sort;
        }
    }

}
