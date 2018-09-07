package com.y3tu.tool.core.util;

import java.io.*;

/**
 * 深度克隆,被克隆的对象必须实现Serializable接口
 *
 * @author y3tu
 * @date 2018/9/8
 */
public class CloneUtils {

    /**
     * 深度克隆
     * @param data 被克隆的对象
     * @param <T>
     * @return 克隆的对象
     */
    public static <T> T deepClone(final Serializable data) {
        if (data == null) return null;
        //noinspection unchecked
        return (T) bytes2Object(serializable2Bytes((Serializable) data));
    }

    /**
     * 实现了Serializable接口的对象转换成byte字节
     *
     * @param serializable
     * @return
     */
    private static byte[] serializable2Bytes(final Serializable serializable) {
        if (serializable == null) return null;
        ByteArrayOutputStream baos;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos = new ByteArrayOutputStream());
            oos.writeObject(serializable);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * byte字节转换成对象
     *
     * @param bytes
     * @return
     */
    private static Object bytes2Object(final byte[] bytes) {
        if (bytes == null) return null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
