package com.y3tu.tool.core.util;

/**
 * 字符串处理工具类
 *
 * @author y3tu
 *
 * @see cn.hutool.core.util.StrUtil
 */
public class StrUtil extends cn.hutool.core.util.StrUtil {

    /**
     * 去掉字符串中的空白字符
     *
     * @param str
     * @return
     */
    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        } else {
            int sz = str.length();
            char[] chs = new char[sz];
            int count = 0;

            for(int i = 0; i < sz; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    chs[count++] = str.charAt(i);
                }
            }

            if (count == sz) {
                return str;
            } else {
                return new String(chs, 0, count);
            }
        }
    }
}
