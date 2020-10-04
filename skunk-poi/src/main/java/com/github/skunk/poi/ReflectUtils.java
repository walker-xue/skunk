package com.github.skunk.poi;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;

import com.github.skunk.core.utils.StringUtils;
import com.github.skunk.poi.file.ExcelColumn;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author nanfeng
 * @date 2019年12月29日
 * @since 0.0.1
 */
@Slf4j
public class ReflectUtils {

    public static <F> Method getGetMethod(Class<F> clazz, String fieldName) throws NoSuchMethodException {

        Objects.requireNonNull(clazz);
        Validate.notBlank(fieldName, "fieldName is null.");

        return clazz.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
    }

    public static <F, V> Method getSetMethod(Class<F> clazz, String fieldName, Class<V> filedValueType) throws NoSuchMethodException {
        Objects.requireNonNull(clazz);
        Validate.notBlank(fieldName, "fieldName is null.");

        return clazz.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), filedValueType);
    }

    public static <O> Object getProperty(Class<O> clazz, O obj, Field field) throws Exception {

        Objects.requireNonNull(clazz);
        Objects.requireNonNull(field);

        try {
            return field.get(obj);
        } catch (Exception e) {
            String name = field.getName();
            Method getter = getGetMethod(clazz, name);
            return getter.invoke(obj);
        }
    }

    private static <O> void setProperty(Class<O> clazz, O obj, Field getField, Object value) throws Exception {

        Objects.requireNonNull(clazz);
        Objects.requireNonNull(obj);
        Objects.requireNonNull(getField);

        String name = getField.getName();
        try {
            Field setField = clazz.getField(name);
            setField.set(obj, value);
        } catch (Exception e) {
            Method setter = getSetMethod(clazz, name, getField.getType());
            setter.invoke(obj, value);
        }
    }

    public static <F, S extends F> void fillSubtype(Class<F> clazz, F father, S son) {

        Objects.requireNonNull(clazz);
        Objects.requireNonNull(father);
        Objects.requireNonNull(son);

        for (Field field : clazz.getDeclaredFields()) {
            try {
                Object value = getProperty(clazz, father, field);
                setProperty(clazz, son, field, value);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public static <C1, C2> void fillFiled(Class<C1> clazz1, C1 src, Class<C2> clazz2, C2 tar) {

        Objects.requireNonNull(clazz1);
        Objects.requireNonNull(src);
        Objects.requireNonNull(clazz2);

        for (Field field : clazz1.getDeclaredFields()) {
            try {
                Object value = getProperty(clazz1, src, field);
                setProperty(clazz2, tar, field, value);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public static Field getField(Object bean, String propertyName) {
        try {
            return bean.getClass().getField(propertyName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getBeanProperty(Object bean, String propertyName) {
        try {
            Method getter = getGetMethod(bean.getClass(), propertyName);
            if (getter != null) {
                return getter.invoke(bean);
            }
            Field field = getField(bean, propertyName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(bean);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getProperty2(Class<?> clazz, Object obj, Field field) throws Exception {

        Objects.requireNonNull(clazz);
        Objects.requireNonNull(obj);
        Objects.requireNonNull(field);

        try {
            return field.get(obj);
        } catch (Exception e) {
            String name = field.getName();
            Method getter = getGetMethod(clazz, name);
            return getter.invoke(obj);
        }
    }

    public static Map<String, Object> getFileds(Object o) {
        Map<String, Object> ret = new HashMap<String, Object>();
        for (Field field : o.getClass().getDeclaredFields()) {
            try {
                Object value = getProperty2(o.getClass(), o, field);
                ret.put(field.getName(), value);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return ret;

    }

    public static <T> T populateBean(T obj, Map<String, Object> map, Map<String, Integer> filedTypeMap) {
        if (map == null || obj == null) {
            return null;
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (field == null) {
                    continue;
                }
                if (map.containsKey(field.getName())) {
                    field.setAccessible(true);
                    String type = field.getGenericType().toString();
                    Object object;
                    Object o = map.get(field.getName());
                    if (o == null) {
                        continue;
                    }
                    String value = o.toString();
                    if (StringUtils.isEmpty(value)) {
                        continue;
                    }
                    if (type.equals("class java.lang.String")) {
                        object = value;
                    } else if (type.equals("class java.lang.Float")) {
                        object = Float.parseFloat(value);
                    } else if (type.equals("class java.lang.Double")) {
                        object = Double.parseDouble(value);
                    } else if (type.equals("class java.lang.Integer")) {
                        if (value.contains(".")) {
                            String[] split = value.split("\\.");
                            object = Integer.parseInt(split[0]);
                        } else {
                            object = Integer.parseInt(value);
                        }
                    } else if (type.equals("class java.lang.Long")) {
                        object = Long.parseLong(value);
                    } else if (type.equals("class java.lang.Boolean")) {
                        object = Boolean.parseBoolean(value);
                    } else if (type.equals("class java.util.Date")) {
                        if (o instanceof Long) {
                            object = new Date((Long) o);
                        } else {
                            try {
                                object = getDateObject(filedTypeMap, field, value);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        object = value;
                    }
                    try {
                        field.set(obj, object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj;
    }

    private static Date getDateObject(Map<String, Integer> filedTypeMap, Field field, String value) throws ParseException {
        Date object = null;
        if (filedTypeMap != null && !filedTypeMap.isEmpty()) {
            if (filedTypeMap.containsKey(field.getName())) {
                Integer type = filedTypeMap.get(field.getName());
                if (type == null) {
                    object = getDateObject(value);
                } else if (type == ExcelColumn.DATE_6) {
                    object = DateUtils.parseDate(value, "yyyy-MM");
                } else if (type == ExcelColumn.DATE_16) {
                    object = DateUtils.parseDate(value, "yyyy-MM-dd HH:mm:ss");
                } else {
                    object = DateUtils.parseDate(value, "yyyy-MM-dd");
                }
            } else {
                object = getDateObject(value);
            }
        } else {
            object = getDateObject(value);
        }
        return object;
    }


    /**
     * 字符串转换成 Date 对象
     *
     * @param value
     * @return
     * @throws ParseException
     */
    private static Date getDateObject(String value) throws ParseException {
        Objects.requireNonNull(value, "date string is null.");

        Date object = null;
        if (StringUtils.filterNull(value).length() == 7) {
            object = DateUtils.parseDate(value, "yyyy-MM");
        } else if (StringUtils.filterNull(value).length() > 7 && StringUtils.filterNull(value).length() <= 10) {
            object = DateUtils.parseDate(value, "yyyy-MM-dd");
        } else if (StringUtils.filterNull(value).length() > 10) {
            object = DateUtils.parseDate(value, "yyyy-MM-dd HH:mm:ss");
        }
        return object;
    }
}
