package com.github.skunk.core.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;

import com.github.skunk.core.collectors.CollectorUtils;

/**
 * @author walker
 * @since 2019年5月15日
 */
public class HibernateValidatorUtils {

    /**
     * 使用hibernate的注解来进行验证
     */
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    static {
        if (validator == null) {
            validator = Validation.buildDefaultValidatorFactory().getValidator();
        }
    }

    /**
     * 功能描述: <br>
     * 〈注解验证参数〉
     *
     * @param obj
     */
    public static <T> Set<ConstraintViolation<T>> validate(T obj) {
        return validator.validate(obj);
    }

    /**
     * 功能描述: <br>
     * 〈注解验证参数〉
     *
     * @param obj
     */
    public static <T> void validate(T obj, Class<?>... groups) {

        Set<ConstraintViolation<T>> validate = validator.validate(obj, groups);

        if (CollectorUtils.isNotEmpty(validate)) {
            throw new ConstraintViolationException(validate);
        }
    }
}