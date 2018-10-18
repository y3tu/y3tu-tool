package com.y3tu.tool.core.convert;

import com.y3tu.tool.core.collection.ArrayUtil;
import com.y3tu.tool.core.util.ObjectUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;


/**
 * 类型转换工具类
 *
 * @author y3tu
 * @date 2018/8/1
 * @see org.apache.commons.beanutils.ConvertUtils 使用ConvertUtils里面的方法
 * @see Converter
 * <p>
 * 1. 需要自己实现 {@code Converter} 接口，并实现convert转换方法，构成转换器
 * 2. 调用register注册转换器，调用ConvertUtil.convert()进行转换
 * 3. 撤销登记的转换器
 * </p>
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

    /**
     * 将基本类型更改为关联的包装器类。
     *
     * @param type 基本类型
     * @param <T>  基本类型
     * @return 包装类型
     */
    public static <T> Class<T> primitiveToWrapper(final Class<T> type) {
        return ConvertUtils.primitiveToWrapper(type);
    }

    /**
     * 转换为long<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Long toLong(Object value, Long defaultValue) {
        if (ObjectUtil.isNull(value)) {
            return defaultValue;
        }
        try {
            return (Long) ConvertUtils.convert(value, Long.class);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为int<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value        被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Integer toInt(Object value, Integer defaultValue) {
        if (ObjectUtil.isNull(value)) {
            return defaultValue;
        }
        try {
            return (Integer) ConvertUtils.convert(value, Integer.class);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为boolean<br>
     * String支持的值为：true、false、yes、ok、no，1,0 如果给定的值为空，或者转换失败，返回默认值<br>
     * 转换失败不会报错
     *
     * @param value 被转换的值
     * @param defaultValue 转换错误时的默认值
     * @return 结果
     */
    public static Boolean toBool(Object value, Boolean defaultValue) {

        if (ObjectUtil.isNull(value)) {
            return defaultValue;
        }
        try {
            return (Boolean) ConvertUtils.convert(value, Boolean.class);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为String数组
     *
     * @param value 被转换的值
     * @return String数组
     */
    public static String[] toStrArray(Object value) {
        return (String[]) ConvertUtil.convert(value, String[].class);
    }

}
