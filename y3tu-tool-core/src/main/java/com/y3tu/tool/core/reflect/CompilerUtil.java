package com.y3tu.tool.core.reflect;

import lombok.extern.slf4j.Slf4j;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 动态编译工具类
 *
 * @author y3tu
 * @date 2018/7/3
 */
@Slf4j
public class CompilerUtil {
    private static JavaCompiler compiler;

    static {
        compiler = ToolProvider.getSystemJavaCompiler();
    }

    /**
     * 编译java文件
     *
     * @param options 编译选项参数
     * @param files   编译的文件
     */
    public static void javac(List<String> options, String... files) {
        StandardJavaFileManager manager = null;
        try {
            manager = compiler.getStandardFileManager(null, null, null);
            Iterable<? extends JavaFileObject> it = manager.getJavaFileObjects(files);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, options, null, it);
            task.call();
            if (log.isDebugEnabled()) {
                for (String file : files) {
                    log.debug("Compile Java File:" + file);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (manager != null) {
                try {
                    manager.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加载类
     *
     * @param name 类名
     * @return
     */
    private static Class<?> load(String name) {
        Class<?> cls = null;
        ClassLoader classLoader = null;
        try {
            classLoader = ClassUtil.class.getClassLoader();
            cls = classLoader.loadClass(name);
            if (log.isDebugEnabled()) {
                log.debug("Load Class[" + name + "] by " + classLoader);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return cls;
    }

    /**
     * 调用类方法
     *
     * @param cls        类
     * @param methodName 方法名
     * @param paramsCls  方法参数类型
     * @param params     方法参数
     * @return
     */
    public static Object invoke(Class<?> cls, String methodName, Class<?>[] paramsCls, Object[] params) {
        Object result = null;
        try {
            Method method = cls.getDeclaredMethod(methodName, paramsCls);
            Object obj = cls.newInstance();
            result = method.invoke(obj, params);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

}
