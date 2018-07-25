package com.y3tu.tool.core.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;

import com.y3tu.tool.core.lang.Assert;
import com.y3tu.tool.core.text.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

/**
 * IO工具类<br>
 * IO工具类只是辅助流的读写，并不负责关闭流。原因是流可能被多次读写，读写关闭后容易造成问题。
 *
 * @author xiaoleilu
 */
@Slf4j
public class IOUtil {

    /**
     * 默认缓存大小
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    /**
     * 默认中等缓存大小
     */
    public static final int DEFAULT_MIDDLE_BUFFER_SIZE = 4096;
    /**
     * 默认大缓存大小
     */
    public static final int DEFAULT_LARGE_BUFFER_SIZE = 8192;

    /**
     * 数据流末尾
     */
    public static final int EOF = -1;

    private static final String CLOSE_ERROR_MESSAGE = "IOException thrown while closing Closeable.";

    private static Logger logger = LoggerFactory.getLogger(IOUtil.class);

    /**
     * 在final中安静的关闭, 不再往外抛出异常避免影响原有异常，最常用函数. 同时兼容Closeable为空未实际创建的情况.
     *
     * @see {@link Closeables#close}
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            logger.warn(CLOSE_ERROR_MESSAGE, e);
        }
    }

    /**
     * 简单读取InputStream到String.
     */
    public static String toString(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input, CharsetUtil.UTF_8);
        return toString(reader);
    }

    /**
     * 简单读取Reader到String
     *
     * @see {@link CharStreams#toString}
     */
    public static String toString(Reader input) throws IOException {
        return CharStreams.toString(input);
    }

    /**
     * 简单读取Reader的每行内容到List<String>
     */
    public static List<String> toLines(final InputStream input) throws IOException {
        return CharStreams.readLines(new BufferedReader(new InputStreamReader(input, CharsetUtil.UTF_8)));
    }

    /**
     * 简单读取Reader的每行内容到List<String>
     *
     * @see {@link CharStreams#readLines}
     */
    public static List<String> toLines(final Reader input) throws IOException {
        return CharStreams.readLines(toBufferedReader(input));
    }

    /**
     * 读取一行数据，比如System.in的用户输入
     */
    public static String readLine(final InputStream input) throws IOException {
        return new BufferedReader(new InputStreamReader(input, CharsetUtil.UTF_8)).readLine();
    }

    /**
     * 从流中读取内容，读取完毕后并不关闭流
     *
     * @param in 输入流，读取完毕后并不关闭流
     * @param charset 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String read(InputStream in, Charset charset) throws IORuntimeException {
        FastByteArrayOutputStream out = read(in);
        return null == charset ? out.toString() : out.toString(charset);
    }

    /**
     * 从流中读取内容，读到输出流中
     *
     * @param in 输入流
     * @return 输出流
     * @throws IORuntimeException IO异常
     */
    public static FastByteArrayOutputStream read(InputStream in) throws IORuntimeException {
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        copy(in, out);
        return out;
    }


    /**
     * 拷贝流，使用默认Buffer大小
     *
     * @param in 输入流
     * @param out 输出流
     * @return 传输的byte数
     * @throws IORuntimeException IO异常
     */
    public static long copy(InputStream in, OutputStream out) throws IORuntimeException {
        return copy(in, out, DEFAULT_BUFFER_SIZE);
    }
    /**
     * 拷贝流
     *
     * @param in 输入流
     * @param out 输出流
     * @param bufferSize 缓存大小
     * @return 传输的byte数
     * @throws IORuntimeException IO异常
     */
    public static long copy(InputStream in, OutputStream out, int bufferSize) throws IORuntimeException {
        return copy(in, out, bufferSize, null);
    }

    /**
     * 拷贝流
     *
     * @param in             输入流
     * @param out            输出流
     * @param bufferSize     缓存大小
     * @param streamProgress 进度条
     * @return 传输的byte数
     * @throws IORuntimeException IO异常
     */
    public static long copy(InputStream in, OutputStream out, int bufferSize, StreamProgress streamProgress) throws IORuntimeException {
        Assert.notNull(in, "InputStream is null !");
        Assert.notNull(out, "OutputStream is null !");
        if (bufferSize <= 0) {
            bufferSize = DEFAULT_BUFFER_SIZE;
        }

        byte[] buffer = new byte[bufferSize];
        long size = 0;
        if (null != streamProgress) {
            streamProgress.start();
        }
        try {
            for (int readSize = -1; (readSize = in.read(buffer)) != EOF; ) {
                out.write(buffer, 0, readSize);
                size += readSize;
                out.flush();
                if (null != streamProgress) {
                    streamProgress.progress(size);
                }
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        if (null != streamProgress) {
            streamProgress.finish();
        }
        return size;
    }

    /**
     * 读取一行数据
     */
    public static String readLine(final Reader reader) throws IOException {
        return toBufferedReader(reader).readLine();
    }

    /**
     * 简单写入String到OutputStream.
     */
    public static void write(final String data, final OutputStream output) throws IOException {
        if (data != null) {
            output.write(data.getBytes(CharsetUtil.UTF_8));
        }
    }

    /**
     * 简单写入String到Writer.
     */
    public static void write(final String data, final Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * 在Reader与Writer间复制内容
     *
     * @see {@link CharStreams#copy}
     */
    public static long copy(final Reader input, final Writer output) throws IOException {
        return CharStreams.copy(input, output);
    }


    public static BufferedReader toBufferedReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }


}
