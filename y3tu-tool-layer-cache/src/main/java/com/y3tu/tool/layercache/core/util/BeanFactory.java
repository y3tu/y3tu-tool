package com.y3tu.tool.layercache.core.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean 工厂类
 *
 * @author yuhao.wang3
 */
@Slf4j
public class BeanFactory {

    /**
     * bean 容器
     */
    private static ConcurrentHashMap<Class, Object> beanContainer = new ConcurrentHashMap<>();

    public static <T> T getBean(Class<T> aClass) {
        return (T) beanContainer.computeIfAbsent(aClass, aClass1 -> {
            try {
                return aClass1.newInstance();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return null;
        });
    }
}
