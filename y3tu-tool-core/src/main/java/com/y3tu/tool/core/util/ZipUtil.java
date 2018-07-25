package com.y3tu.tool.core.util;

import com.y3tu.tool.core.exception.UtilException;
import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.text.CharsetUtil;

import java.io.File;
import java.nio.charset.Charset;

/**
 * 压缩工具类
 *
 * @author y3tu
 * @date 2018/7/25
 */
public class ZipUtil {
    /** 默认编码，使用平台相关编码 */
    private static final Charset DEFAULT_CHARSET = CharsetUtil.defaultCharset();

    /**
     * 打包到当前目录，使用默认编码UTF-8
     *
     * @param srcPath 源文件路径
     * @return 打包好的压缩文件
     * @throws UtilException IO异常
     */
    public static File zip(String srcPath) throws UtilException {
        return zip(srcPath, DEFAULT_CHARSET);
    }

    /**
     * 打包到当前目录
     *
     * @param srcPath 源文件路径
     * @param charset 编码
     * @return 打包好的压缩文件
     * @throws UtilException IO异常
     */
    public static File zip(String srcPath, Charset charset) throws UtilException {
       // return zip(FileUtil.touch(srcPath), charset);
        return null;
    }

    /**
     * 打包到当前目录，使用默认编码UTF-8
     *
     * @param srcFile 源文件或目录
     * @return 打包好的压缩文件
     * @throws UtilException IO异常
     */
    public static File zip(File srcFile) throws UtilException {
       // return zip(srcFile, DEFAULT_CHARSET);
        return null;
    }
}
