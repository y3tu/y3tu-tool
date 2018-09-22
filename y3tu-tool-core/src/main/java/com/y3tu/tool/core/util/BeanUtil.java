package com.y3tu.tool.core.util;

import com.y3tu.tool.core.exception.UtilException;
import com.y3tu.tool.core.reflect.ClassUtil;
import org.apache.commons.beanutils.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean工具类
 *
 * <p>
 * 把一个拥有对属性进行set和get方法的类，我们就可以称之为JavaBean。
 * </p>
 *
 * @author y3tu
 */
public class BeanUtil extends BeanUtils {
    /**
     * 判断是否为Bean对象<br>
     * 判定方法是是否存在只有一个参数的setXXX方法
     *
     * @param clazz 待测试类
     * @return 是否为Bean对象
     */
    public static boolean isBean(Class<?> clazz) {
        if (ClassUtil.isNormalClass(clazz)) {
            final Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")) {
                    // 检测包含标准的setXXX方法即视为标准的JavaBean
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * bean转换成map
     *
     * @param obj 需要转换的bean
     * @return
     * @throws Exception
     */
    public static Map<String, Object> beanToMap(Object obj) {
        //obj为空，结束方法
        if (obj == null)
            return null;
        Map<String, Object> map = new HashMap<>();
        //Introspector 类为通过工具学习有关受目标 Java Bean 支持的属性、事件和方法的知识提供了一个标准方法。
        // java的自省机制
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] ps = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : ps) {
                String key = propertyDescriptor.getName();

                if (!"class".equals(key)) {
                    Method getter = propertyDescriptor.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new UtilException("bean转换成map失败：" + e.getMessage());
        }
        return map;
    }

    /**
     * map转换成bean
     *
     * @param map   需要转换的map
     * @param clazz 目标类型
     * @param <T>
     * @return 转换后的bean
     * @throws Exception
     */
    public static <T extends Object> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        try {
            T instance = clazz.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : descriptors) {
                String key = property.getName();
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    Method setter = property.getWriteMethod();
                    setter.invoke(instance, value);
                }
            }
            return instance;
        } catch (Exception e) {
            throw new UtilException("map转换成bean失败：" + e.getMessage());
        }
    }

}
