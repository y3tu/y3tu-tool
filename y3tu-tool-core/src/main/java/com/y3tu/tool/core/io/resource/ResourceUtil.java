package com.y3tu.tool.core.io.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.y3tu.tool.core.collection.ListUtil;
import com.y3tu.tool.core.io.FilePathUtil;
import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.io.IORuntimeException;
import com.y3tu.tool.core.reflect.ClassLoaderUtil;

/**
 * ClassPath资源工具类
 *
 * @author y3tu
 */
public class ResourceUtil {

    /**
     * 获取单个资源的Url
     *
     * @param resourceName 资源名
     * @return 资源名url
     */
    public static URL asUrl(String resourceName) {
        return Resources.getResource(resourceName);
    }

    public static URL asUrlByPath(String path) {
        try {
            if (FilePathUtil.isAbsolutePath(path)) {
                return FileUtil.file(path).toURI().toURL();
            } else {
                return Resources.getResource(path);
            }
        }catch (Exception e){
            throw new IORuntimeException(e,"获取配置文件错误");
        }
    }

    /**
     * 获取单个资源的Url
     *
     * @param contextClass class
     * @param resourceName 资源名
     * @return 资源名url
     */
    public static URL asUrl(Class<?> contextClass, String resourceName) {
        return (null != contextClass) ? contextClass.getResource(resourceName) : ClassLoaderUtil.getClassLoader().getResource(resourceName);
    }


    /**
     * 获取单个资源的输入流
     *
     * @param resourceName 资源名
     * @return 资源输入流
     */
    public static InputStream asStream(String resourceName) throws IOException {
        return Resources.getResource(resourceName).openStream();
    }

    /**
     * 获取单个资源的输入流
     *
     * @param contextClass class
     * @param resourceName 资源名
     * @return 资源输入流
     */
    public static InputStream asStream(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.getResource(contextClass, resourceName).openStream();
    }


    /**
     * 读取资源的每一行
     *
     * @param resourceName 资源名
     */
    public static String toString(String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8);
    }

    /**
     * 读取文件的每一行
     *
     * @param contextClass class
     * @param resourceName 资源名
     */
    public static String toString(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(contextClass, resourceName), Charsets.UTF_8);
    }

    /**
     * 读取文件的每一行,并把数据展示在一行
     *
     * @param resourceName 资源名
     */
    public static List<String> toLines(String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(resourceName), Charsets.UTF_8);
    }

    /**
     * 读取文件的每一行，并把数据展示在一行
     *
     * @param contextClass class
     * @param resourceName 资源名
     */
    public static List<String> toLines(Class<?> contextClass, String resourceName) throws IOException {
        return Resources.readLines(Resources.getResource(contextClass, resourceName), Charsets.UTF_8);
    }

    /**
     * 获取所有同名的资源URL集合
     *
     * @param resourceName 资源名
     * @return
     */
    public static List<URL> getResourcesQuietly(String resourceName) {
        return getResourcesQuietly(resourceName, ClassLoaderUtil.getClassLoader());
    }

    /**
     * 获取所有同名的资源URL集合
     *
     * @param resourceName       资源名
     * @param contextClassLoader class
     * @return
     */
    public static List<URL> getResourcesQuietly(String resourceName, ClassLoader contextClassLoader) {
        try {
            Enumeration<URL> urls = contextClassLoader.getResources(resourceName);
            List<URL> list = new ArrayList<URL>(10);
            while (urls.hasMoreElements()) {
                list.add(urls.nextElement());
            }
            return list;
        } catch (IOException e) {
            return ListUtil.emptyList();
        }
    }


}
