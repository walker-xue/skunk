package com.skunk.acl;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.util.Objects;

import org.springframework.aop.framework.AopProxyUtils;

/**
 * 注解工具类
 *
 * @author walker
 * @date 2019.6.11
 * @since 0.0.1
 */
public class AnnotationUtils {

    /**
     * @param cls
     * @param annotationClass
     * @param <T>
     * @return
     */
    private static <T extends Annotation> T getAnnotation(Class<?> cls, Class<T> annotationClass) {

        if (Objects.isNull(cls) || Objects.isNull(annotationClass)) {
            return null;
        }
        T res = cls.getAnnotation(annotationClass);
        if (res == null) {
            for (Annotation annotation : cls.getAnnotations()) {
                if (annotation instanceof Documented) {
                    continue;
                }
                res = getAnnotation(annotation.annotationType(), annotationClass);
                if (res != null) {
                    break;
                }
            }
        }
        return res;
    }

    /**
     * @param obj
     * @param annotationClass
     * @param <Obj>
     * @param <T>
     * @return
     */
    public static <Obj, T extends Annotation> T getAnnotation(Obj obj, Class<T> annotationClass) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return getAnnotation(AopProxyUtils.ultimateTargetClass(obj), annotationClass);
    }

    /**
     * @param obj
     * @param <Obj>
     * @return
     */
    public static <Obj> String getResourceType(Obj obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        ResourceInfo info = getAnnotation(obj, ResourceInfo.class);
        return Objects.isNull(obj) ? null : info.type();
    }

    /**
     * @param obj
     * @param <Obj>
     * @return
     */
    public static <Obj> String getFunctionName(Obj obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        SelfDefined definedSearch = getAnnotation(obj, SelfDefined.class);
        return Objects.isNull(definedSearch) ? null : definedSearch.functionName();
    }
}
