package com.y3tu.tool.core.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * bean实例缓存,防止重复创建bean实例
 *
 * @author y3tu
 */
@Slf4j
public class BeanCacheUtil {
    /**
     * bean 容器
     */
    private static ConcurrentHashMap<Class, Object> beanContainer = new ConcurrentHashMap<>();

    /**
     * 获取bean缓存，如果不存在则创建bean实例并放入容器
     *
     * @param aClass
     * @param <T>
     * @return
     */
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
