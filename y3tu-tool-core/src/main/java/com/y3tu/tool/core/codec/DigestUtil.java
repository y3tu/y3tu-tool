package com.y3tu.tool.core.codec;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 摘要算法工具类
 *
 * @author y3tu
 * @date 2018/9/24
 * @see org.apache.commons.codec.digest.DigestUtils 主要移植common-codec包里面的方法
 */
public class DigestUtil {

    /**
     * MD5加密
     *
     * @param data 需要加密的字符串
     * @return
     */
    public static String md5Hex(final String data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * MD5加密
     *
     * @param data 需要加密的数据
     * @return
     */
    public static String md5Hex(final byte[] data) {
        return DigestUtils.md5Hex(data);
    }

    /**
     * MD5加密
     *
     * @param data 需要加密的数据
     * @return
     */
    public static String md5Hex(final InputStream data) throws IOException {
        return DigestUtils.md5Hex(data);
    }

    /**
     * 算SHA-1摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return SHA-1摘要的16进制表示
     */
    public static String sha1Hex(final byte[] data) {
        return DigestUtils.sha1Hex(data);
    }

    /**
     * 算SHA-1摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return SHA-1摘要的16进制表示
     */
    public static String sha1Hex(final InputStream data) throws IOException {
        return DigestUtils.sha1Hex(data);
    }

    /**
     * 算SHA-1摘要值，并转为16进制字符串
     *
     * @param data 被摘要数据
     * @return SHA-1摘要的16进制表示
     */
    public static String sha1Hex(final String data) {
        return DigestUtils.sha1Hex(data);
    }

    /**
     * 计算SHA-256摘要值
     *
     * @param data 被摘要数据
     * @return SHA-256摘要的16进制表示
     */
    public static String sha256Hex(final byte[] data) {
        return DigestUtils.sha256Hex(data);
    }

    /**
     * 计算SHA-256摘要值
     *
     * @param data 被摘要数据
     * @return SHA-256摘要的16进制表示
     */
    public static String sha256Hex(final InputStream data) throws IOException {
        return DigestUtils.sha256Hex(data);
    }

    /**
     * 计算SHA-256摘要值
     *
     * @param data 被摘要数据
     * @return SHA-256摘要的16进制表示
     */
    public static String sha256Hex(final String data) {
        return DigestUtils.sha256Hex(data);
    }
}
