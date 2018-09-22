package com.y3tu.tool.core.io;

import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.collection.ListUtil;
import com.y3tu.tool.core.io.resource.ResourceUtil;
import com.y3tu.tool.core.lang.Platforms;

import com.google.common.io.Files;
import com.y3tu.tool.core.reflect.ClassUtil;
import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.core.util.URLUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;


/**
 * 关于文件路径的工具集
 */
public class FilePathUtil {


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

        List<String> pathList = ListUtil.newArrayList(StringUtils.splitPreserveAllTokens(pathToUse, StringUtils.C_SLASH));
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
            // 对于jar中文件包含file:前缀，需要去掉此类前缀，在此做标准化，解决中文或空格路径被编码的问题
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

    /**
     * 文件路径是否相同<br>
     * 取两个文件的绝对路径比较，在Windows下忽略大小写，在Linux下不忽略。
     *
     * @param file1 文件1
     * @param file2 文件2
     * @return 文件路径是否相同
     */
    public static boolean pathEquals(File file1, File file2) {
        if (FileUtil.isWindows()) {
            // Windows环境
            try {
                if (StringUtils.equalsIgnoreCase(file1.getCanonicalPath(), file2.getCanonicalPath())) {
                    return true;
                }
            } catch (Exception e) {
                if (StringUtils.equalsIgnoreCase(file1.getAbsolutePath(), file2.getAbsolutePath())) {
                    return true;
                }
            }
        } else {
            // 类Unix环境
            try {
                if (StringUtils.equals(file1.getCanonicalPath(), file2.getCanonicalPath())) {
                    return true;
                }
            } catch (Exception e) {
                if (StringUtils.equals(file1.getAbsolutePath(), file2.getAbsolutePath())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获得最后一个文件路径分隔符的位置
     *
     * @param filePath 文件路径
     * @return 最后一个文件路径分隔符的位置
     */
    public static int lastIndexOfSeparator(String filePath) {
        if (filePath == null) {
            return -1;
        }
        int lastUnixPos = filePath.lastIndexOf(FileUtil.UNIX_SEPARATOR);
        int lastWindowsPos = filePath.lastIndexOf(FileUtil.WINDOWS_SEPARATOR);
        return (lastUnixPos >= lastWindowsPos) ? lastUnixPos : lastWindowsPos;
    }

    /**
     * 获得相对子路径
     * <p>
     * 栗子：
     *
     * <pre>
     * dirPath: d:/aaa/bbb    filePath: d:/aaa/bbb/ccc     =》    ccc
     * dirPath: d:/Aaa/bbb    filePath: d:/aaa/bbb/ccc.txt     =》    ccc.txt
     * </pre>
     *
     * @param rootDir 绝对父路径
     * @param file    文件
     * @return 相对子路径
     */
    public static String subPath(String rootDir, File file) {
        try {
            return subPath(rootDir, file.getCanonicalPath());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获得相对子路径，忽略大小写
     * <p>
     * 栗子：
     *
     * <pre>
     * dirPath: d:/aaa/bbb    filePath: d:/aaa/bbb/ccc     =》    ccc
     * dirPath: d:/Aaa/bbb    filePath: d:/aaa/bbb/ccc.txt     =》    ccc.txt
     * dirPath: d:/Aaa/bbb    filePath: d:/aaa/bbb/     =》    ""
     * </pre>
     *
     * @param dirPath  父路径
     * @param filePath 文件路径
     * @return 相对子路径
     */
    public static String subPath(String dirPath, String filePath) {
        if (StringUtils.isNotEmpty(dirPath) && StringUtils.isNotEmpty(filePath)) {

            dirPath = StringUtils.removeSuffix(FilePathUtil.normalize(dirPath), "/");
            filePath = FilePathUtil.normalize(filePath);

            final String result = StringUtils.removePrefixIgnoreCase(filePath, dirPath);
            return StringUtils.removePrefix(result, "/");
        }
        return filePath;
    }

    /**
     * 获取指定位置的子路径部分，支持负数，例如index为-1表示从后数第一个节点位置
     *
     * @param path  路径
     * @param index 路径节点位置，支持负数（负数从后向前计数）
     * @return 获取的子路径
     */
    public static Path getPathEle(Path path, int index) {
        return subPath(path, index, index == -1 ? path.getNameCount() : index + 1);
    }

    /**
     * 获取指定位置的最后一个子路径部分
     *
     * @param path 路径
     * @return 获取的最后一个子路径
     */
    public static Path getLastPathEle(Path path) {
        return getPathEle(path, path.getNameCount() - 1);
    }

    /**
     * 获取指定位置的子路径部分，支持负数，例如起始为-1表示从后数第一个节点位置
     *
     * @param path      路径
     * @param fromIndex 起始路径节点（包括）
     * @param toIndex   结束路径节点（不包括）
     * @return 获取的子路径
     */
    public static Path subPath(Path path, int fromIndex, int toIndex) {
        if (null == path) {
            return null;
        }
        final int len = path.getNameCount();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        } else if (fromIndex > len) {
            fromIndex = len;
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
            if (toIndex < 0) {
                toIndex = len;
            }
        } else if (toIndex > len) {
            toIndex = len;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return null;
        }
        return path.subpath(fromIndex, toIndex);
    }

    /**
     * 判断文件路径是否有指定后缀，忽略大小写<br>
     * 常用语判断扩展名
     *
     * @param file   文件或目录
     * @param suffix 后缀
     * @return 是否有指定后缀
     */
    public static boolean pathEndsWith(File file, String suffix) {
        return file.getPath().toLowerCase().endsWith(suffix);
    }

    /**
     * 获取Web项目下的web root路径
     *
     * @return web root路径
     */
    public static File getWebRoot() {
        String classPath = ClassUtil.getClassPath();
        if (StringUtils.isNotBlank(classPath)) {
            return FileUtil.file(classPath).getParentFile().getParentFile();
        }
        return null;
    }
}
