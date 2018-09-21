package com.y3tu.tool.core.convert;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;


/**
 * 类型转换工具类
 * @see org.apache.commons.beanutils.ConvertUtils 使用ConvertUtils里面的方法
 * @see Converter
 * <p>
 *    1. 需要自己实现 {@code Converter} 接口，并实现convert转换方法，构成转换器
 *    2. 调用register注册转换器，调用ConvertUtil.convert()进行转换
 *    3. 撤销登记的转换器
 * </p>
 * @author y3tu
 * @date 2018/8/1
 */
public class ConvertUtil {

    /**
     * 转换类型
     *
     * @param value      需要转换的值
     * @param targetType 目标类型
     * @return 转换后的值
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
