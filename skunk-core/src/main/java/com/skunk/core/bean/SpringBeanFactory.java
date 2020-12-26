package com.skunk.core.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 从Ｓｐｒｉｎｇ容器中获取Bean
 * <p>
 * 注：必须让Ｓｐｒｉｎｇ扫描到该组件，不然不能从SpringBeanFactory获取到对应的ｂｅａｎ
 *
 * @author walker
 * @since 0.0.1
 */
@Component
public class SpringBeanFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;
    private static String MESSAGE = "SpringBeanFactory没有被Spring容器扫描到，请在扫描组件位置加入`com.skunk`包后，再试一下.";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanFactory.applicationContext = applicationContext;
    }

    /**
     * 通过beanName 获取Bean对象
     *
     * @param beanName
     * @return 返回Object进行强转
     */
    public static Object beanByName(String beanName) {
        if (applicationContext == null) {
            throw new RuntimeException(MESSAGE);
        }
        return applicationContext.getBean(beanName);
    }

    /**
     * 通过Class获取Bean对象
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T bean(Class<T> type) {
        if (applicationContext == null) {
            throw new RuntimeException(MESSAGE);
        }
        return applicationContext.getBean(type);
    }
}