package com.y3tu.tool.core.reflect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 获取Class信息的工具类
 *
 * @author y3tu
 * @date 2018/7/3
 */
@Slf4j
public class ClassUtil {

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    /**
     * 返回短Class名, 不包含PackageName.
     * <p>
     * 内部类的话，返回"主类.内部类"
     */
    public static String getShortClassName(final Class<?> cls) {
        return ClassUtils.getShortClassName(cls);
    }

    /**
     * 返回Class名，不包含PackageName
     * <p>
     * 内部类的话，返回"主类.内部类"
     */
    public static String getShortClassName(final String className) {
        return ClassUtils.getShortClassName(className);
    }

    /**
     * 返回Class的PackageName
     */
    public static String getPackageName(final Class<?> cls) {
        return ClassUtils.getPackageName(cls);
    }

    /**
     * 返回Class的PackageName
     */
    public static String getPackageName(final String className) {
        return ClassUtils.getPackageName(className);
    }

    /**
     * 递归返回所有的SupperClasses，包含Object.class
     */
    public static List<Class<?>> getAllSuperclasses(final Class<?> cls) {
        return ClassUtils.getAllSuperclasses(cls);
    }

    /**
     * 递归返回本类及所有基类继承的接口，及接口继承的接口，比Spring中的相同实现完整
     */
    public static List<Class<?>> getAllInterfaces(final Class<?> cls) {
        return ClassUtils.getAllInterfaces(cls);
    }

    /**
     * https://github.com/linkedin/linkedin-utils/blob/master/org.linkedin.util-core/src/main/java/org/linkedin/util/reflect/ReflectUtils.java
     * <p>
     * The purpose of this method is somewhat to provide a better naming / documentation than the javadoc of
     * <code>Class.isAssignableFrom</code> method.
     *
     * @return <code>true</code> if subclass is a subclass or sub interface of superclass
     */
    public static boolean isSubClassOrInterfaceOf(Class subclass, Class superclass) {
        return superclass.isAssignableFrom(subclass);
    }

    /**
     * 获取CGLib处理过后的实体的原Class.
     */
    public static Class<?> unwrapCglib(Object instance) {
        Validate.notNull(instance, "Instance must not be null");
        Class<?> clazz = instance.getClass();
        if ((clazz != null) && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if ((superClass != null) && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型,
     * <p>
     * 注意泛型必须定义在父类处. 这是唯一可以通过反射从泛型获得Class实例的地方.
     * <p>
     * 如无法找到, 返回Object.class.
     * <p>
     * eg. public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    public static <T> Class<T> getClassGenericType(final Class clazz) {
        return getClassGenericType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * <p>
     * 注意泛型必须定义在父类处. 这是唯一可以通过反射从泛型获得Class实例的地方.
     * <p>
     * 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic declaration, start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public static Class getClassGenericType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if ((index >= params.length) || (index < 0)) {
            log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }
}
