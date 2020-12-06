package com.github.skunk.core.utils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Bean 转化 Map or Map 转 Bean
 * <p>
 */
@Slf4j
public class BeanConvertUtils {

    /**
     * 方法说明：对象转换
     *  
     *
     * @param source
     *     原对象
     * @param target
     *     目标对象
     * @param ignoreProperties
     *     排除要copy的属性
     * @return
     */
    public static <T> T copy(Object source, Class<T> target, String... ignoreProperties) {

        try {
            T targetInstance = target.newInstance();
            if (ArrayUtils.isEmpty(ignoreProperties)) {
                BeanUtils.copyProperties(source, targetInstance);
            } else {
                BeanUtils.copyProperties(source, targetInstance, ignoreProperties);
            }
            return targetInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法说明：对象转换(List)
     *  
     *
     * @param list
     *     原对象
     * @param target
     *     目标对象
     * @param ignoreProperties
     *     排除要copy的属性
     * @return
     */
    public static <T, E> List<T> copyList(List<E> list, Class<T> target, String... ignoreProperties) {

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<T> targetList = new ArrayList<>();
        for (E e : list) {
            targetList.add(copy(e, target, ignoreProperties));
        }
        return targetList;
    }

    /**
     * 方法说明：map转化为对象
     *  
     *
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToObject(Map<String, Object> map, T bean) {

        Validate.notNull(bean, "Map to bean. bean is null");

        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 方法说明：map转化为对象
     *  
     *
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        try {
            T bean = clazz.newInstance();
            return mapToObject(map, bean);
        } catch (InstantiationException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取对象的属性值
     *
     * @param propertyName
     * @param obj
     * @return 属性值
     */
    public static Object getObjectValue(String propertyName, Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        if (obj instanceof Map) {
            return ((Map) obj).get(propertyName);
        }
        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(obj.getClass(), propertyName);
        try {
            return descriptor.getReadMethod().invoke(obj, new Object[] {});
        } catch (Exception e) {
            log.error("descriptor ReadMethod().invoke error. ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * bean to map
     *
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> toMap(T bean) {
        if (Objects.isNull(bean)) {
            return Collections.emptyMap();
        }
        Map<String, Object> map = new ConcurrentHashMap<>();
        BeanMap beanMap = BeanMap.create(bean);
        for (Object key : beanMap.keySet()) {
            map.put(key.toString(), beanMap.get(key));
        }
        return map;
    }
}
