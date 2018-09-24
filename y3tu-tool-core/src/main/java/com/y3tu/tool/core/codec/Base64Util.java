package com.y3tu.tool.core.codec;

import com.y3tu.tool.core.exception.UtilException;
import com.y3tu.tool.core.text.CharsetUtil;
import com.y3tu.tool.core.text.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64编码
 *
 * @author y3tu
 * @date 2018/9/24
 * <p>
 * 采用jdk1.8自带的Base64编码解码
 * @see java.util.Base64
 */
public class Base64Util {

    /**
     * 默认编码UTF-8
     */
    private static final String DEFAULT_CHARSET = CharsetUtil.UTF_8;

    /**
     * 编码
     *
     * @param source 被编码的byte数组
     * @return 编码后的byte数组
     */
    public static byte[] encode(byte[] source) {
        if (source.length == 0) {
            return source;
        }
        return Base64.getEncoder().encode(source);
    }

    /**
     * 编码
     *
     * @param source 被编码的字符串
     * @return 编码后的字符串
     */
    public static String encodeStr(String source) {
        return encodeStr(source, DEFAULT_CHARSET);
    }

    /**
     * 编码
     *
     * @param source  被编码的字符串
     * @param charset 字符集
     * @return 编码后的字符串
     */
    public static String encodeStr(String source, String charset) {
        try {
            byte[] bytes = source.getBytes(charset);
            return StringUtils.str(encode(bytes), charset);
        } catch (UnsupportedEncodingException e) {
            throw new UtilException("base64编码异常!", e);
        }
    }

    /**
     * 编码，URL安全的
     *
     * @param source 被编码的base64 byte数组
     * @return 被加密后的字符串
     */
    public static byte[] encodeUrlSafe(byte[] source) {
        if (source.length == 0) {
            return source;
        }
        return Base64.getUrlEncoder().encode(source);
    }

    /**
     * 编码，URL安全的
     *
     * @param source  被编码的base64 byte数组
     * @param charset 字符集
     * @return 被加密后的字符串
     */
    public String encodeUrlSafe(String source, String charset) {
        try {
            return StringUtils.str(Base64.getUrlEncoder().encode(source.getBytes(charset)), charset);
        } catch (UnsupportedEncodingException e) {
            throw new UtilException("base64编码异常!", e);
        }
    }


    /**
     * 解码
     *
     * @param src 被解码的base64字符
     * @return 解码后的字符
     */
    public static byte[] decode(byte[] src) {
        if (src.length == 0) {
            return src;
        }
        return Base64.getDecoder().decode(src);
    }

    /**
     * 解码
     *
     * @param source  被解码的字符串
     * @param charset 字符集
     * @return
     */
    public static String decodeStr(String source, String charset) {
        try {
            byte[] bytes = source.getBytes(charset);
            return StringUtils.str(decode(bytes), charset);
        } catch (UnsupportedEncodingException e) {
            throw new UtilException("base64解码异常!", e);
        }
    }


    /**
     * 解码，URL安全的
     *
     * @param source 被解码的byte数组
     * @return 解码后的byte数组
     */
    public static byte[] decodeUrlSafe(byte[] source) {
        if (source.length == 0) {
            return source;
        }
        return Base64.getUrlDecoder().decode(source);
    }

    /**
     * 解码，URL安全的
     *
     * @param source  被解码的字符串
     * @param charset 字符集
     * @return 解码后的字符串
     */
    public String decodeUrlSafe(String source, String charset) {
        try {
            return StringUtils.str(Base64.getUrlDecoder().decode(source.getBytes(charset)), charset);
        } catch (UnsupportedEncodingException e) {
            throw new UtilException("base64解码异常!", e);
        }
    }

}
