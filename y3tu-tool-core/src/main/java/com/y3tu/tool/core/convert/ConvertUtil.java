package com.y3tu.tool.core.convert;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

/**
 * 类型转换工具类
 * 使用org.apache.commons.beanutils.ConvertUtils里面的方法
 *
 * @author y3tu
 * @date 2018/8/1
 */
public class ConvertUtil {

    /**
     * 转换类型
     *
     * @param value      需要转换的值
     * @param targetType 目标类型
     * @return
     */
    public static Object convert(Object value, Class<?> targetType) {
        return ConvertUtils.convert(value, targetType);
    }

    /**
     * 查看目标类型的转换器
     *
     * @param clazz 目标类型
     * @return
     */
    public static Converter lookup(Class<?> clazz) {
        return ConvertUtils.lookup(clazz);
    }

    /**
     * 给特定类型登记(绑定)转换器
     *
     * @param converter 转换器
     * @param clazz     需要转换的类型
     */
    public static void register(Converter converter, Class<?> clazz) {
        ConvertUtils.register(converter, clazz);
    }

    /**
     * 撤销登记的转换器Converter
     *
     * @param clazz 登记的class
     */
    public static void deregister(Class<?> clazz) {
        ConvertUtils.deregister(clazz);
    }

    /**
     * 撤销所以登记的转换器Converter
     */
    public static void deregister() {
        ConvertUtils.deregister();
    }


}
