package com.y3tu.tool.core.util;

import com.y3tu.tool.core.exception.UtilException;
import com.y3tu.tool.core.lang.Editor;
import com.y3tu.tool.core.reflect.ClassUtil;
import com.y3tu.tool.core.text.StringUtils;
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
 *
 * @author y3tu
 * @see BeanUtils 主要使用org.apache.commons.beanutils.BeanUtils里面的方法
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
     * 对象转Map，不进行驼峰转下划线，不忽略值为空的字段
     *
     * @param bean bean对象
     * @return Map
     */
    public static Map<String, Object> beanToMap(Object bean) {
        return beanToMap(bean, false, false);
    }

    /**
     * 对象转Map
     *
     * @param bean              bean对象
     * @param isToUnderlineCase 是否转换为下划线模式
     * @param ignoreNullValue   是否忽略值为空的字段
     * @return Map
     */
    public static Map<String, Object> beanToMap(Object bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
        return beanToMap(bean, new HashMap<String, Object>(), isToUnderlineCase, ignoreNullValue);
    }

    /**
     * 对象转Map
     *
     * @param bean              bean对象
     * @param targetMap         目标的Map
     * @param isToUnderlineCase 是否转换为下划线模式
     * @param ignoreNullValue   是否忽略值为空的字段
     * @return Map
     */
    public static Map<String, Object> beanToMap(Object bean, Map<String, Object> targetMap, final boolean isToUnderlineCase, boolean ignoreNullValue) {
        if (bean == null) {
            return null;
        }

        return beanToMap(bean, targetMap, ignoreNullValue, new Editor<String>() {

            @Override
            public String edit(String key) {
                return isToUnderlineCase ? StringUtils.toUnderlineCase(key) : key;
            }
        });
    }

    /**
     * 对象转Map<br>
     * 通过实现{@link Editor} 可以自定义字段值，如果这个Editor返回null则忽略这个字段，以便实现：
     *
     * <pre>
     * 1. 字段筛选，可以去除不需要的字段
     * 2. 字段变换，例如实现驼峰转下划线
     * 3. 自定义字段前缀或后缀等等
     * </pre>
     *
     * @param obj             bean对象
     * @param targetMap       目标的Map
     * @param ignoreNullValue 是否忽略值为空的字段
     * @param keyEditor       属性字段（Map的key）编辑器，用于筛选、编辑key
     * @return Map
     */
    public static Map<String, Object> beanToMap(Object obj, Map<String, Object> targetMap, boolean ignoreNullValue, Editor<String> keyEditor) {
        //obj为空，结束方法
        if (obj == null) {
            return null;
        }
        if (targetMap == null) {
            targetMap = new HashMap<>();
        }
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
                    if (false == ignoreNullValue || (null != value && false == value.equals(obj))) {
                        key = keyEditor.edit(key);
                        if (null != key) {
                            targetMap.put(key, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new UtilException("bean转换成map失败：" + e.getMessage());
        }
        return targetMap;
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
