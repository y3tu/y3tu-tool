package com.y3tu.tool.core.io;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import com.y3tu.tool.core.annotation.NotNull;
import com.y3tu.tool.core.annotation.Nullable;
import com.y3tu.tool.core.collection.ArrayUtil;
import com.y3tu.tool.core.lang.Assert;
import com.y3tu.tool.core.lang.Platforms;
import com.y3tu.tool.core.text.CharUtil;
import com.y3tu.tool.core.text.CharsetUtil;
import com.y3tu.tool.core.text.StringUtils;
import org.apache.commons.lang3.Validate;


/**
 * 文件工具类
 *
 * @author xiaoleilu
 */
public class FileUtil {
    /**
     * 类Unix路径分隔符
     */
    private static final char UNIX_SEPARATOR = CharUtil.SLASH;
    /**
     * Windows路径分隔符
     */
    private static final char WINDOWS_SEPARATOR = CharUtil.BACKSLASH;
    /**
     * Windows下文件名中的无效字符
     */
    private static Pattern FILE_NAME_INVALID_PATTERN_WIN = Pattern.compile("[\\\\/:*?\"<>|]");

    /**
     * Class文件扩展名
     */
    public static final String CLASS_EXT = ".class";
    /**
     * Jar文件扩展名
     */
    public static final String JAR_FILE_EXT = ".jar";
    /**
     * 在Jar中的路径jar的扩展名形式
     */
    public static final String JAR_PATH_EXT = ".jar!";

    /**
     * 是否为Windows环境
     *
     * @return 是否为Windows环境
     */
    public static boolean isWindows() {
        return WINDOWS_SEPARATOR == File.separatorChar;
    }

    /**
     * 列出目录文件<br>
     * 给定的绝对路径不能是压缩包中的路径
     *
     * @param path 目录绝对路径或者相对路径
     * @return 文件列表（包含目录）
     */
    public static File[] ls(String path) {
        if (path == null) {
            return null;
        }

        path = FilePathUtil.getAbsolutePath(path, null);

        File file = new File(path);
        if (file.isDirectory()) {
            return file.listFiles();
        }
        throw new IORuntimeException(StringUtils.format("Path [{}] is not directory!", path));
    }

    /**
     * 文件是否为空<br>
     * 目录：里面没有文件时为空 文件：文件大小为0时为空
     *
     * @param file 文件
     * @return 是否为空，当提供非目录时，返回false
     */
    public static boolean isEmpty(File file) {
        if (null == file) {
            return true;
        }

        if (file.isDirectory()) {
            String[] subFiles = file.list();
            if (ArrayUtil.isEmpty(subFiles)) {
                return true;
            }
        } else if (file.isFile()) {
            return file.length() <= 0;
        }

        return false;
    }

    /**
     * 递归遍历目录以及子目录中的所有文件<br>
     * 如果提供file为文件，直接返回过滤结果
     *
     * @param file       当前遍历文件或目录
     * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录
     * @return 文件列表
     */
    public static List<File> loopFiles(File file, FileFilter fileFilter) {
        List<File> fileList = new ArrayList<File>();
        if (null == file) {
            return fileList;
        } else if (false == file.exists()) {
            return fileList;
        }

        if (file.isDirectory()) {
            final File[] subFiles = file.listFiles();
            if (ArrayUtil.isNotEmpty(subFiles)) {
                for (File tmp : subFiles) {
                    fileList.addAll(loopFiles(tmp, fileFilter));
                }
            }
        } else {
            if (null == fileFilter || fileFilter.accept(file)) {
                fileList.add(file);
            }
        }

        return fileList;
    }

    /**
     * 获得指定目录下所有文件<br>
     * 不会扫描子目录
     *
     * @param path 相对ClassPath的目录或者绝对路径目录
     * @return 文件路径列表（如果是jar中的文件，则给定类似.jar!/xxx/xxx的路径）
     * @throws IORuntimeException IO异常
     */
    public static List<String> listFileNames(String path) throws IORuntimeException {
        if (path == null) {
            return null;
        }
        List<String> paths = new ArrayList<String>();

        int index = path.lastIndexOf(FileUtil.JAR_PATH_EXT);
        if (index == -1) {
            // 普通目录路径
            File[] files = ls(path);
            for (File file : files) {
                if (file.isFile()) {
                    paths.add(file.getName());
                }
            }
        } else {
            // jar文件
            path = FilePathUtil.getAbsolutePath(path);
            if (false == path.endsWith(String.valueOf(UNIX_SEPARATOR))) {
                path = path + UNIX_SEPARATOR;
            }
            // jar文件中的路径
            index = index + FileUtil.JAR_FILE_EXT.length();
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(path.substring(0, index));
                final String subPath = path.substring(index + 2);
                for (JarEntry entry : Collections.list(jarFile.entries())) {
                    final String name = entry.getName();
                    if (name.startsWith(subPath)) {
                        final String nameSuffix = StringUtils.removePrefix(name, subPath);
                        if (nameSuffix.contains(String.valueOf(UNIX_SEPARATOR)) == false) {
                            paths.add(nameSuffix);
                        }
                    }
                }
            } catch (IOException e) {
                throw new IORuntimeException(StringUtils.format("Can not read file path of [{}]", path), e);
            } finally {
                IOUtil.close(jarFile);
            }
        }
        return paths;
    }

    /**
     * 计算目录或文件的总大小<br>
     * 当给定对象为文件时，直接调用 {@link File#length()}<br>
     * 当给定对象为目录时，遍历目录下的所有文件和目录，递归计算其大小，求和返回
     *
     * @param file 目录或文件
     * @return 总大小，bytes长度
     */
    public static long size(File file) {
        Assert.notNull(file, "file argument is null !");
        if (false == file.exists()) {
            throw new IllegalArgumentException(StringUtils.format("File [{}] not exist !", file.getAbsolutePath()));
        }

        if (file.isDirectory()) {
            long size = 0L;
            File[] subFiles = file.listFiles();
            if (ArrayUtil.isEmpty(subFiles)) {
                // empty directory
                return 0L;
            }
            for (int i = 0; i < subFiles.length; i++) {
                size += size(subFiles[i]);
            }
            return size;
        } else {
            return file.length();
        }
    }

    private static FileVisitor<Path> deleteFileVisitor = new SimpleFileVisitor<Path>() {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };

    //////// 文件读写//////

    /**
     * 读取文件到byte[].
     *
     * @see {@link Files#readAllBytes}
     */
    public static byte[] toByteArray(final File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    /**
     * 读取文件到String.
     */
    public static String toString(final File file) throws IOException {
        return com.google.common.io.Files.toString(file, CharsetUtil.UTF_8);
    }

    /**
     * 读取文件的每行内容到List<String>.
     *
     * @see {@link Files#readAllLines}
     */
    public static List<String> toLines(final File file) throws IOException {
        return Files.readAllLines(file.toPath(), CharsetUtil.UTF_8);
    }

    /**
     * 简单写入String到File.
     */
    public static void write(final CharSequence data, final File file) throws IOException {
        Validate.notNull(file);
        Validate.notNull(data);

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), CharsetUtil.UTF_8)) {
            writer.append(data);
        }
    }

    /**
     * 追加String到File.
     */
    public static void append(final CharSequence data, final File file) throws IOException {
        Validate.notNull(file);
        Validate.notNull(data);

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), CharsetUtil.UTF_8,
                StandardOpenOption.APPEND)) {
            writer.append(data);
        }
    }

    /**
     * 打开文件为InputStream.
     *
     * @see {@link Files#newInputStream}
     */
    public static InputStream asInputStream(String fileName) throws IOException {
        return asInputStream(getPath(fileName));
    }

    /**
     * 打开文件为InputStream.
     *
     * @see {@link Files#newInputStream}
     */
    public static InputStream asInputStream(File file) throws IOException {
        Validate.notNull(file, "file is null");
        return asInputStream(file.toPath());
    }

    /**
     * 打开文件为InputStream.
     *
     * @see {@link Files#newInputStream}
     */
    public static InputStream asInputStream(Path path) throws IOException {
        Validate.notNull(path, "path is null");
        return Files.newInputStream(path);
    }

    /**
     * 打开文件为OutputStream.
     *
     * @see {@link Files#newOutputStream}
     */
    public static OutputStream asOututStream(String fileName) throws IOException {
        return asOututStream(getPath(fileName));
    }

    /**
     * 打开文件为OutputStream.
     *
     * @see {@link Files#newOutputStream}
     */
    public static OutputStream asOututStream(File file) throws IOException {
        Validate.notNull(file, "file is null");
        return asOututStream(file.toPath());
    }

    /**
     * 打开文件为OutputStream.
     *
     * @see {@link Files#newOutputStream}
     */
    public static OutputStream asOututStream(Path path) throws IOException {
        Validate.notNull(path, "path is null");
        return Files.newOutputStream(path);
    }

    /**
     * 获取File的BufferedReader.
     *
     * @see {@link Files#newBufferedReader}
     */
    public static BufferedReader asBufferedReader(String fileName) throws IOException {
        Validate.notBlank(fileName, "filename is blank");
        return asBufferedReader(getPath(fileName));
    }

    public static BufferedReader asBufferedReader(Path path) throws IOException {
        Validate.notNull(path, "path is null");
        return Files.newBufferedReader(path, CharsetUtil.UTF_8);
    }

    /**
     * 获取File的BufferedWriter.
     *
     * @see {@link Files#newBufferedWriter}
     */
    public static BufferedWriter asBufferedWriter(String fileName) throws IOException {
        Validate.notBlank(fileName, "filename is blank");
        return Files.newBufferedWriter(getPath(fileName), CharsetUtil.UTF_8);
    }

    /**
     * 获取File的BufferedWriter.
     *
     * @see {@link Files#newBufferedWriter}
     */
    public static BufferedWriter asBufferedWriter(Path path) throws IOException {
        Validate.notNull(path, "path is null");
        return Files.newBufferedWriter(path, CharsetUtil.UTF_8);
    }

    ///// 文件操作 /////

    /**
     * 复制文件或目录, not following links.
     *
     * @param from 如果为null，或者是不存在的文件或目录，抛出异常.
     * @param to   如果为null，或者from是目录而to是已存在文件，或相反
     */
    public static void copy(@NotNull File from, @NotNull File to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);

        copy(from.toPath(), to.toPath());
    }

    /**
     * 复制文件或目录, not following links.
     *
     * @param from 如果为null，或者是不存在的文件或目录，抛出异常.
     * @param to   如果为null，或者from是目录而to是已存在文件，或相反
     */
    public static void copy(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);

        if (Files.isDirectory(from)) {
            copyDir(from, to);
        } else {
            copyFile(from, to);
        }
    }

    /**
     * 文件复制.
     *
     * @param from 如果为null，或文件不存在或者是目录，，抛出异常
     * @param to   如果to为null，或文件存在但是一个目录，抛出异常
     * @see {@link Files#copy}
     */
    public static void copyFile(@NotNull File from, @NotNull File to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);
        copyFile(from.toPath(), to.toPath());
    }

    /**
     * 文件复制. @see {@link Files#copy}
     *
     * @param from 如果为null，或文件不存在或者是目录，，抛出异常
     * @param to   如果to为null，或文件存在但是一个目录，抛出异常
     */
    public static void copyFile(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.isTrue(Files.exists(from), "%s is not exist or not a file", from);
        Validate.notNull(to);
        Validate.isTrue(!FileUtil.isDirExists(to), "%s is exist but it is a dir", to);
        Files.copy(from, to);
    }

    /**
     * 复制目录
     */
    public static void copyDir(@NotNull File from, @NotNull File to) throws IOException {
        Validate.isTrue(isDirExists(from), "%s is not exist or not a dir", from);
        Validate.notNull(to);

        copyDir(from.toPath(), to.toPath());
    }

    /**
     * 复制目录
     */
    public static void copyDir(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.isTrue(isDirExists(from), "%s is not exist or not a dir", from);
        Validate.notNull(to);
        makesureDirExists(to);

        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(from)) {
            for (Path path : dirStream) {
                copy(path, to.resolve(path.getFileName()));
            }
        }
    }

    /**
     * 文件移动/重命名.
     *
     * @see {@link Files#move}
     */
    public static void moveFile(@NotNull File from, @NotNull File to) throws IOException {
        Validate.notNull(from);
        Validate.notNull(to);

        moveFile(from.toPath(), to.toPath());
    }

    /**
     * 文件移动/重命名.
     *
     * @see {@link Files#move}
     */
    public static void moveFile(@NotNull Path from, @NotNull Path to) throws IOException {
        Validate.isTrue(isFileExists(from), "%s is not exist or not a file", from);
        Validate.notNull(to);
        Validate.isTrue(!isDirExists(to), "%s is  exist but it is a dir", to);

        Files.move(from, to);
    }

    /**
     * 目录移动/重命名
     */
    public static void moveDir(@NotNull File from, @NotNull File to) throws IOException {
        Validate.isTrue(isDirExists(from), "%s is not exist or not a dir", from);
        Validate.notNull(to);
        Validate.isTrue(!isFileExists(to), "%s is exist but it is a file", to);

        final boolean rename = from.renameTo(to);
        if (!rename) {
            if (to.getCanonicalPath().startsWith(from.getCanonicalPath() + File.separator)) {
                throw new IOException("Cannot move directory: " + from + " to a subdirectory of itself: " + to);
            }
            copyDir(from, to);
            deleteDir(from);
            if (from.exists()) {
                throw new IOException("Failed to delete original directory '" + from + "' after copy to '" + to + '\'');
            }
        }
    }

    /**
     * 创建文件或更新时间戳.
     *
     * @see {@link com.google.common.io.Files#touch}
     */
    public static void touch(String filePath) throws IOException {
        touch(new File(filePath));
    }

    /**
     * 创建文件或更新时间戳.
     *
     * @see {@link com.google.common.io.Files#touch}
     */
    public static void touch(File file) throws IOException {
        com.google.common.io.Files.touch(file);
    }

    /**
     * 删除文件.
     * <p>
     * 如果文件不存在或者是目录，则不做修改
     */
    public static void deleteFile(@Nullable File file) throws IOException {
        Validate.isTrue(isFileExists(file), "%s is not exist or not a file", file);
        deleteFile(file.toPath());
    }

    /**
     * 删除文件.
     * <p>
     * 如果文件不存在或者是目录，则不做修改
     */
    public static void deleteFile(@Nullable Path path) throws IOException {
        Validate.isTrue(isFileExists(path), "%s is not exist or not a file", path);

        Files.delete(path);
    }

    /**
     * 删除目录及所有子目录/文件
     *
     * @see {@link Files#walkFileTree}
     */
    public static void deleteDir(Path dir) throws IOException {
        Validate.isTrue(isDirExists(dir), "%s is not exist or not a dir", dir);

        // 后序遍历，先删掉子目录中的文件/目录
        Files.walkFileTree(dir, deleteFileVisitor);
    }

    /**
     * 删除目录及所有子目录/文件
     */
    public static void deleteDir(File dir) throws IOException {
        Validate.isTrue(isDirExists(dir), "%s is not exist or not a dir", dir);
        deleteDir(dir.toPath());
    }

    /**
     * 判断目录是否存在, from Jodd
     */
    public static boolean isDirExists(String dirPath) {
        if (dirPath == null) {
            return false;
        }
        return isDirExists(getPath(dirPath));
    }

    public static boolean isDirExists(Path dirPath) {
        return dirPath != null && Files.exists(dirPath) && Files.isDirectory(dirPath);
    }

    /**
     * 判断目录是否存在, from Jodd
     */
    public static boolean isDirExists(File dir) {
        if (dir == null) {
            return false;
        }
        return isDirExists(dir.toPath());
    }

    /**
     * 确保目录存在, 如不存在则创建
     */
    public static void makesureDirExists(String dirPath) throws IOException {
        makesureDirExists(getPath(dirPath));
    }

    /**
     * 确保目录存在, 如不存在则创建
     */
    public static void makesureDirExists(File file) throws IOException {
        Validate.notNull(file);
        makesureDirExists(file.toPath());
    }

    /**
     * 确保目录存在, 如不存在则创建.
     *
     * @see {@link Files#createDirectories}
     */
    public static void makesureDirExists(Path dirPath) throws IOException {
        Validate.notNull(dirPath);
        Files.createDirectories(dirPath);
    }

    /**
     * 确保父目录及其父目录直到根目录都已经创建.
     */
    public static void makesureParentDirExists(File file) throws IOException {
        Validate.notNull(file);
        makesureDirExists(file.getParentFile());
    }

    /**
     * 判断文件是否存在, from Jodd.
     *
     * @see {@link Files#exists}
     * @see {@link Files#isRegularFile}
     */
    public static boolean isFileExists(String fileName) {
        if (fileName == null) {
            return false;
        }
        return isFileExists(getPath(fileName));
    }

    /**
     * 判断文件是否存在, from Jodd.
     *
     * @see {@link Files#exists}
     * @see {@link Files#isRegularFile}
     */
    public static boolean isFileExists(File file) {
        if (file == null) {
            return false;
        }
        return isFileExists(file.toPath());
    }

    /**
     * 判断文件是否存在, from Jodd.
     *
     * @see {@link Files#exists}
     * @see {@link Files#isRegularFile}
     */
    public static boolean isFileExists(Path path) {
        if (path == null) {
            return false;
        }
        return Files.exists(path) && Files.isRegularFile(path);
    }

    /**
     * 在临时目录创建临时目录，命名为${毫秒级时间戳}-${同一毫秒内的随机数}.
     *
     * @see {@link Files#createTempDirectory}
     */
    public static Path createTempDir() throws IOException {
        return Files.createTempDirectory(System.currentTimeMillis() + "-");
    }

    /**
     * 在临时目录创建临时文件，命名为tmp-${random.nextLong()}.tmp
     *
     * @see {@link Files#createTempFile}
     */
    public static Path createTempFile() throws IOException {
        return Files.createTempFile("tmp-", ".tmp");
    }

    /**
     * 在临时目录创建临时文件，命名为${prefix}${random.nextLong()}${suffix}
     *
     * @see {@link Files#createTempFile}
     */
    public static Path createTempFile(String prefix, String suffix) throws IOException {
        return Files.createTempFile(prefix, suffix);
    }

    private static Path getPath(String filePath) {
        return Paths.get(filePath);
    }

    /**
     * 获取文件名(不包含路径)
     */
    public static String getFileName(@NotNull String fullName) {
        Validate.notEmpty(fullName);
        int last = fullName.lastIndexOf(Platforms.FILE_PATH_SEPARATOR_CHAR);
        return fullName.substring(last + 1);
    }

    /**
     * 获取文件名的扩展名部分(不包含.)
     *
     * @see {@link com.google.common.io.Files#getFileExtension}
     */
    public static String getFileExtension(File file) {
        return com.google.common.io.Files.getFileExtension(file.getName());
    }

    /**
     * 获取文件名的扩展名部分(不包含.)
     *
     * @see {@link com.google.common.io.Files#getFileExtension}
     */
    public static String getFileExtension(String fullName) {
        return com.google.common.io.Files.getFileExtension(fullName);
    }
}
