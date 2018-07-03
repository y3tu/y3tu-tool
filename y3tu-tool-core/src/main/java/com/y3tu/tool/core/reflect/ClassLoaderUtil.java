package com.y3tu.tool.core.reflect;

/**
 * 类加载器工具类
 *
 * @author y3tu
 * @date 2018/7/3
 * <p>
 * Copy from Spring, 按顺序获取默认ClassLoader
 * <p>
 * 1. Thread.currentThread().getContextClassLoader()
 * <p>
 * 2. ClassLoaderUtil的加载ClassLoader
 * <p>
 * 3. SystemClassLoader
 */
public class ClassLoaderUtil {

    /**
     * 得到默认的类加载器
     *
     * @return
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassLoaderUtil.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    /**
     * 探测类是否存在classpath中
     *
     * @param className   类名
     * @param classLoader 类加载器
     * @return
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            classLoader.loadClass(className);
            return true;
        } catch (Throwable ex) {
            // Class or one of its dependencies is not present...
            return false;
        }
    }
}
