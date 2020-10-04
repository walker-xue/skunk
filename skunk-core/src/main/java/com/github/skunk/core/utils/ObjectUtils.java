package com.github.skunk.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;

import com.github.skunk.core.io.FastByteArrayOutputStream;

/**
 * 对象操作工具类
 *
 * @author nanfeng
 * @date 2019年12月10日
 * @since 0.0.1
 */
@Slf4j
public class ObjectUtils {

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
            return ((Map<?, ?>) obj).values().contains(element);
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
        return !isNull(obj);
    }

    /**
     * 克隆对象<br>
     * <p> 对象必须实现Serializable接口</p>
     *
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(T obj) {
        final FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(obj);
            out.flush();
            final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray()));
            return (T) in.readObject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
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
        FastByteArrayOutputStream byteOut = new FastByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(byteOut)) {
            oos.writeObject(t);
            oos.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return byteOut.toByteArray();
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
            log.error(e.getMessage(), e);
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
                if (((Double) obj).isInfinite() || ((Double) obj).isNaN()) {
                    return false;
                }
            } else if (obj instanceof Float) {
                if (((Float) obj).isInfinite() || ((Float) obj).isNaN()) {
                    return false;
                }
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
    public static <T, R> List<R> beanCopyPropertiesForList(List<T> sourceList, Class<R> targetCls) {
        List<R> targetList = new ArrayList<R>();
        if (sourceList != null && !sourceList.isEmpty()) {
            for (T source : sourceList) {
                targetList.add(beanCopyProperties(source, targetCls));
            }
        }

        return targetList;
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
    public static <R> R beanCopyProperties(Object source, Class<R> targetCls) {

        try {
            R target = targetCls.getDeclaredConstructor().newInstance();
            BeanCopier copier = BeanCopier.create(source.getClass(), targetCls, false);
            if (source != null) {
                copier.copy(source, target, null);
            }
            return target;
        } catch (InstantiationException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Instantiation target error.");
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Target Illegal Access error.");
        } catch (InvocationTargetException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Invocation Target error.");
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Target class No Such Method error.");
        }
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

        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());

        List<String> proList = properties != null ? Arrays.asList(properties) : null;

        for (PropertyDescriptor targetPd : targetPds) {
            if ((targetPd.getWriteMethod() == null) || (proList == null || !proList.contains(targetPd.getName()))) {
                continue;
            }
            PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
            if ((sourcePd == null) || (sourcePd.getReadMethod() == null)) {
                continue;
            }
            try {
                Method readMethod = sourcePd.getReadMethod();
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                Object value = readMethod.invoke(source, new Object[0]);
                Method writeMethod = targetPd.getWriteMethod();
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }
                writeMethod.invoke(target, new Object[] { value });
            } catch (Throwable ex) {
                log.error("Could not copy properties from source to target", ex);
            }
        }
    }
}
