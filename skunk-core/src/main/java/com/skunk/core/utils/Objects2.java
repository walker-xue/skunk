package com.skunk.core.utils;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.CollectionUtils;

import com.skunk.core.collectors.Collection2Utils;
import com.skunk.core.io.FastByteArrayOutputStream;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 对象操作工具类
 *
 * @author nanfeng
 * @date 2019年12月10日
 * @since 0.0.1
 */
@Slf4j
public class Objects2 {

    /**
     * 检查src是否为空，若为空则抛出NullPointerException异常
     * “” 为空
     * “ ” 为空
     * null 为空
     *
     * @param src
     */
    public static void requireNotBlank(String src) {
        requireNotBlank(src, null);
    }

    /**
     * 检查src是否为空，若为空则抛出NullPointerException异常
     * “” 为空
     * “ ” 为空
     * null 为空
     *
     * @param src
     * @param message
     *     异常提示信息
     */
    public static void requireNotBlank(String src, String message) {
        if (String2Utils.isBlank(src)) {
            throw new NullPointerException(message);
        }
    }

    /**
     * 比较两个对象是否相等。<br>
     * 相同的条件有两个，满足其一即可：<br>
     * <code>
     * 1. obj1 == null &amp;&amp; obj2 == null;
     * 2. obj1.equals(obj2)
     * </code>
     *
     * @param obj1
     *     对象1
     * @param obj2
     *     对象2
     * @return 是否相等
     */
    @Deprecated
    public static boolean equals(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    /**
     * <p>计算对象长度，如果是字符串调用其length函数，集合类调用其size函数，数组调用其length属性，其他可遍历对象遍历计算长度</p>
     *
     * @param obj
     *     被计算长度的对象
     * @return 长度
     */
    public static int length(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length();
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size();
        }

        int count;
        if (obj instanceof Iterator) {
            Iterator<?> iter = (Iterator<?>) obj;
            count = 0;
            while (iter.hasNext()) {
                count++;
                iter.next();
            }
            return count;
        }
        if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) obj;
            count = 0;
            while (enumeration.hasMoreElements()) {
                count++;
                enumeration.nextElement();
            }
            return count;
        }
        if (obj.getClass().isArray() == true) {
            return Array.getLength(obj);
        }
        return -1;
    }

    /**
     * 对象中是否包含元素
     *
     * @param obj
     *     对象
     * @param element
     *     元素
     * @return 是否包含
     */
    public static boolean contains(Object obj, Object element) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            if (element == null) {
                return false;
            }
            return ((String) obj).contains(element.toString());
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).contains(element);
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).containsValue(element);
        }

        if (obj instanceof Iterator) {
            Iterator<?> iter = (Iterator<?>) obj;
            while (iter.hasNext()) {
                Object o = iter.next();
                if (equals(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) obj;
            while (enumeration.hasMoreElements()) {
                Object o = enumeration.nextElement();
                if (equals(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj.getClass().isArray() == true) {
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object o = Array.get(obj, i);
                if (equals(o, element)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查对象是否为null
     *
     * @param obj
     *     对象
     * @return 是否为null
     */
    public static boolean isNull(Object obj) {
        return Objects.isNull(obj);
    }

    /**
     * 检查对象是否不为null
     *
     * @param obj
     *     对象
     * @return 是否为null
     */
    public static boolean isNotNull(Object obj) {
        return !Objects.isNull(obj);
    }

    /**
     * 序列化<br>
     * 对象必须实现Serializable接口
     *
     * @param <T>
     * @param t
     *     要被序列化的对象
     * @return 序列化后的字节码
     */
    public static <T> byte[] serialize(T t) {

        Objects.requireNonNull(t);

        try (FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(byteOut)) {
            oos.writeObject(t);
            oos.flush();
            return byteOut.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化<br>
     * 对象必须实现Serializable接口
     *
     * @param <T>
     * @param bytes
     *     反序列化的字节码
     * @return 反序列化后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T unserialize(byte[] bytes) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查是否为有效的数字<br>
     * 检查Double和Float是否为无限大，或者Not a Number<br>
     * 非数字类型和Null将返回true
     *
     * @param obj
     *     被检查类型
     * @return 检查结果，非数字类型和Null将返回true
     */
    public static boolean isValidIfNumber(Object obj) {
        if (obj != null && obj instanceof Number) {
            if (obj instanceof Double) {
                return !((Double) obj).isInfinite() && !((Double) obj).isNaN();
            } else if (obj instanceof Float) {
                return !((Float) obj).isInfinite() && !((Float) obj).isNaN();
            }
        }
        return true;
    }

    /**
     * 将List中的对象拷贝到目标对象的List中(标准Bean)
     *
     * @param sourceList
     *     源List
     * @param targetCls
     *     目标对象类型
     * @param <T>
     *     源类型
     * @param <R>
     *     目标类型
     * @return 目标类型List数组
     */
    public static <T, R> List<R> beanCopyForList(List<T> sourceList, Class<R> targetCls) {

        if (Collection2Utils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }

        return sourceList.stream()
            .map(item -> beanCopyForBean(item, targetCls)).collect(Collectors.toList());
    }

    /**
     * 属性值拷贝(标准Bean)
     *
     * @param source
     *     源对象
     * @param targetCls
     *     目标对象类
     * @return 拷贝目标类的实体
     */
    public static <R> R beanCopyForBean(Object source, Class<R> targetCls) {

        if (Objects.isNull(source)) {
            return null;
        }

        try {
            R target = targetCls.getDeclaredConstructor().newInstance();
            BeanCopier copier = BeanCopier.create(source.getClass(), targetCls, false);
            copier.copy(source, target, null);
            return target;
        } catch (InstantiationException e) {
            throw new RuntimeException("Instantiation target error.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Target Illegal Access error.");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Invocation Target error.");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Target class No Such Method error.");
        }
    }

    /**
     * 克隆对象<br>
     * <p> 对象必须实现Serializable接口</p>
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T copy(T obj) {
        final FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(obj);
            out.flush();
            final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray()));
            return (T) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 方法说明：对象转换
     * Apache Commons BeanUtils
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

        if (Objects.isNull(source)) {
            return null;
        }
        try {
            T targetInstance = target.newInstance();
            if (ArrayUtils.isEmpty(ignoreProperties)) {
                org.springframework.beans.BeanUtils.copyProperties(source, targetInstance);
            } else {
                org.springframework.beans.BeanUtils.copyProperties(source, targetInstance, ignoreProperties);
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
    public static <T, E> List<T> copyForList(List<E> list, Class<T> target, String... ignoreProperties) {

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
    public static <T> Optional<T> mapToBean(Map<String, Object> map, T bean) {

        if (Objects.isNull(map)) {
            return Optional.empty();
        }

        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return Optional.of(bean);
    }

    /**
     * 方法说明：map转化为对象
     *  
     *
     * @param map
     * @param clazz
     * @return
     */
    @SneakyThrows
    public static <T> Optional<T> mapToBean(Map<String, Object> map, Class<T> clazz) {

        if (Objects.isNull(map)) {
            return Optional.empty();
        }

        try {
            T bean = clazz.getDeclaredConstructor().newInstance();
            return mapToBean(map, bean);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
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
    @SneakyThrows
    public static Object getObjectPropertyValue(String propertyName, Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        if (obj instanceof Map) {
            return ((Map) obj).get(propertyName);
        }
        PropertyDescriptor descriptor = org.springframework.beans.BeanUtils.getPropertyDescriptor(obj.getClass(), propertyName);
        return descriptor.getReadMethod().invoke(obj);
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

    /**
     * 复制对象得属性到新的对象
     * <p>
     * 可以指定对象要复制的属性
     *
     * @param source
     *     　源对象
     * @param target
     *     　目标对象
     * @param properties
     *     　指定的属性
     */
    @Deprecated
    public static void beanCopyProperties(Object source, Object target, String[] properties) {

        PropertyDescriptor[] targetPds = org.springframework.beans.BeanUtils.getPropertyDescriptors(target.getClass());

        List<String> proList = properties != null ? Arrays.asList(properties) : null;

        for (PropertyDescriptor targetPd : targetPds) {
            if ((targetPd.getWriteMethod() == null) || (proList == null || !proList.contains(targetPd.getName()))) {
                continue;
            }
            PropertyDescriptor sourcePd = org.springframework.beans.BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
            if ((sourcePd == null) || (sourcePd.getReadMethod() == null)) {
                continue;
            }
            try {
                Method readMethod = sourcePd.getReadMethod();
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                Object value = readMethod.invoke(source);
                Method writeMethod = targetPd.getWriteMethod();
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                writeMethod.invoke(target, value);
            } catch (Throwable ex) {
                log.error("Could not copy properties from source to target", ex);
            }
        }
    }
}
