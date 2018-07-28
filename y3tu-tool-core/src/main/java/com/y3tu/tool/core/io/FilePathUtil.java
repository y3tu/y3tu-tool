package com.y3tu.tool.core.io;

import com.y3tu.tool.core.collection.ArrayUtil;
import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.io.resource.ResourceUtil;
import com.y3tu.tool.core.lang.Platforms;

import com.google.common.io.Files;
import com.y3tu.tool.core.reflect.ClassUtil;
import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.core.util.URLUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;


/**
 * 关于文件路径的工具集. 这个类只适合处理纯字符串的路径，如果是File对象或者Path对象的路径处理，建议直接使用Path类的方法。
 *
 * @see {@link java.nio.file.Path}
 */
public class FilePathUtil {

    /**
     * 在Windows环境里，兼容Windows上的路径分割符，将 '/' 转回 '\'
     */
    public static String normalizePath(String path) {
        if (Platforms.FILE_PATH_SEPARATOR_CHAR == Platforms.WINDOWS_FILE_PATH_SEPARATOR_CHAR
                && StringUtils.indexOf(path, Platforms.LINUX_FILE_PATH_SEPARATOR_CHAR) != -1) {
            return StringUtils.replaceChars(path, Platforms.LINUX_FILE_PATH_SEPARATOR_CHAR,
                    Platforms.WINDOWS_FILE_PATH_SEPARATOR_CHAR);
        }
        return path;

    }


    /**
     * 将路径整理，如 "a/../b"，整理成 "b"
     */
    public static String simplifyPath(String path) {
        return Files.simplifyPath(path);
    }


    /**
     * 以拼接路径名
     */
    public static String concat(String baseName, String... appendName) {
        if (appendName.length == 0) {
            return baseName;
        }

        StringBuilder concatName = new StringBuilder();
        if (StringUtils.endWith(baseName, Platforms.FILE_PATH_SEPARATOR_CHAR)) {
            concatName.append(baseName).append(appendName[0]);
        } else {
            concatName.append(baseName).append(Platforms.FILE_PATH_SEPARATOR_CHAR).append(appendName[0]);
        }

        if (appendName.length > 1) {
            for (int i = 1; i < appendName.length; i++) {
                concatName.append(Platforms.FILE_PATH_SEPARATOR_CHAR).append(appendName[i]);
            }
        }

        return concatName.toString();
    }

    /**
     * 获得上层目录的路径
     */
    public static String getParentPath(String path) {
        String parentPath = path;

        if (Platforms.FILE_PATH_SEPARATOR.equals(parentPath)) {
            return parentPath;
        }

        parentPath = StringUtils.removeEnd(parentPath, Platforms.FILE_PATH_SEPARATOR_CHAR);

        int idx = parentPath.lastIndexOf(Platforms.FILE_PATH_SEPARATOR_CHAR);
        if (idx >= 0) {
            parentPath = parentPath.substring(0, idx + 1);
        } else {
            parentPath = Platforms.FILE_PATH_SEPARATOR;
        }

        return parentPath;
    }

    /**
     * 获得参数clazz所在的Jar文件的绝对路径
     */
    public static String getJarPath(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
    }

    /**
     * 修复路径<br>
     * 如果原路径尾部有分隔符，则保留为标准分隔符（/），否则不保留
     * <ol>
     * <li>1. 统一用 /</li>
     * <li>2. 多个 / 转换为一个 /</li>
     * <li>3. 去除两边空格</li>
     * <li>4. .. 和 . 转换为绝对路径，当..多于已有路径时，直接返回根路径</li>
     * </ol>
     * <p>
     * 栗子：
     *
     * <pre>
     * "/foo//" =》 "/foo/"
     * "/foo/./" =》 "/foo/"
     * "/foo/../bar" =》 "/bar"
     * "/foo/../bar/" =》 "/bar/"
     * "/foo/../bar/../baz" =》 "/baz"
     * "/../" =》 "/"
     * "foo/bar/.." =》 "foo"
     * "foo/../bar" =》 "bar"
     * "foo/../../bar" =》 "bar"
     * "//server/foo/../bar" =》 "/server/bar"
     * "//server/../bar" =》 "/bar"
     * "C:\\foo\\..\\bar" =》 "C:/bar"
     * "C:\\..\\bar" =》 "C:/bar"
     * "~/foo/../bar/" =》 "~/bar/"
     * "~/../bar" =》 "bar"
     * </pre>
     *
     * @param path 原路径
     * @return 修复后的路径
     */
    public static String normalize(String path) {
        if (path == null) {
            return null;
        }

        // 兼容Spring风格的ClassPath路径，去除前缀，不区分大小写
        String pathToUse = StringUtils.removePrefixIgnoreCase(path, "classpath:");
        // 去除file:前缀
        pathToUse = StringUtils.removePrefixIgnoreCase(pathToUse, "file:");
        // 统一使用斜杠
        pathToUse = pathToUse.replaceAll("[/\\\\]{1,}", "/").trim();

        int prefixIndex = pathToUse.indexOf(StringUtils.COLON);
        String prefix = "";
        if (prefixIndex > -1) {
            // 可能Windows风格路径
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (StringUtils.startWith(prefix, StringUtils.C_SLASH)) {
                // 去除类似于/C:这类路径开头的斜杠
                prefix = prefix.substring(1);
            }
            if (false == prefix.contains("/")) {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            } else {
                // 如果前缀中包含/,说明非Windows风格path
                prefix = StringUtils.EMPTY;
            }
        }
        if (pathToUse.startsWith(StringUtils.SLASH)) {
            prefix += StringUtils.SLASH;
            pathToUse = pathToUse.substring(1);
        }

        List<String> pathList = ArrayUtil.asList(StringUtils.split(pathToUse, StringUtils.C_SLASH));
        List<String> pathElements = new LinkedList<String>();
        int tops = 0;

        String element;
        for (int i = pathList.size() - 1; i >= 0; i--) {
            element = pathList.get(i);
            if (StringUtils.DOT.equals(element)) {
                // 当前目录，丢弃
            } else if (StringUtils.DOUBLE_DOT.equals(element)) {
                tops++;
            } else {
                if (tops > 0) {
                    // 有上级目录标记时按照个数依次跳过
                    tops--;
                } else {
                    // Normal path element found.
                    pathElements.add(0, element);
                }
            }
        }

        return prefix + CollectionUtil.join(pathElements, StringUtils.SLASH);
    }

    /**
     * 获取绝对路径<br>
     * 此方法不会判定给定路径是否有效（文件或目录存在）
     *
     * @param path      相对路径
     * @param baseClass 相对路径所相对的类
     * @return 绝对路径
     */
    public static String getAbsolutePath(String path, Class<?> baseClass) {
        String normalPath;
        if (path == null) {
            normalPath = StringUtils.EMPTY;
        } else {
            normalPath = normalize(path);
            if (isAbsolutePath(normalPath)) {
                // 给定的路径已经是绝对路径了
                return normalPath;
            }
        }

        // 相对于ClassPath路径
        final URL url = ResourceUtil.asUrl(baseClass, normalPath);
        if (null != url) {
            // 对于jar中文件包含file:前缀，需要去掉此类前缀，在此做标准化，since 3.0.8 解决中文或空格路径被编码的问题
            return FilePathUtil.normalize(URLUtil.getDecodedPath(url));
        }

        // 如果资源不存在，则返回一个拼接的资源绝对路径
        final String classPath = ClassUtil.getClassPath();
        if (null == classPath) {
            throw new NullPointerException("ClassPath is null !");
        }

        // 资源不存在的情况下使用标准化路径有问题，使用原始路径拼接后标准化路径
        return normalize(classPath.concat(path));
    }

    /**
     * 获取绝对路径，相对于ClassPath的目录<br>
     * 如果给定就是绝对路径，则返回原路径，原路径把所有\替换为/<br>
     * 兼容Spring风格的路径表示，例如：classpath:config/example.setting也会被识别后转换
     *
     * @param path 相对路径
     * @return 绝对路径
     */
    public static String getAbsolutePath(String path) {
        return getAbsolutePath(path, null);
    }

    /**
     * 获取标准的绝对路径
     *
     * @param file 文件
     * @return 绝对路径
     */
    public static String getAbsolutePath(File file) {
        if (file == null) {
            return null;
        }

        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    /**
     * 给定路径已经是绝对路径<br>
     * 此方法并没有针对路径做标准化，建议先执行{@link #normalize(String)}方法标准化路径后判断
     *
     * @param path 需要检查的Path
     * @return 是否已经是绝对路径
     */
    public static boolean isAbsolutePath(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }

        if (StringUtils.C_SLASH == path.charAt(0) || path.matches("^[a-zA-Z]:[/\\\\].*")) {
            // 给定的路径已经是绝对路径了
            return true;
        }
        return false;
    }
}
