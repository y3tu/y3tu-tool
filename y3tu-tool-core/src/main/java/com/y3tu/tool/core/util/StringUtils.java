package com.y3tu.tool.core.util;

/**
 * @author y3tu
 * @date 2018/2/27
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils{

    /**
     * 判断字符是否为空
     *
     * @param str
     * @return 返回类型 boolean 为空则为true，否则为false
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str) || "".equals(str.trim()) || str.equals("null") || str.equals("undefined")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否空白字符
     *
     * @param str
     * @return 是：true，否：false
     */
    public static boolean isBlank(String str) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否空白字符
     *
     * @param str
     * @return 是：false，否：true
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 对象为空
     * @param obj
     * @return true：空；false：非空
     */
    public static boolean isNull(Object obj) {
        return null == obj ? true : false;
    }

    /**
     * 取得指定子串在字符串中出现的次数。
     * <p/>
     * <p>
     * 如果字符串为<code>null</code>或空，则返回<code>0</code>。
     * <pre>
     * StringUtils.countMatches(null, *)       = 0
     * StringUtils.countMatches("", *)         = 0
     * StringUtils.countMatches("abba", null)  = 0
     * StringUtils.countMatches("abba", "")    = 0
     * StringUtils.countMatches("abba", "a")   = 2
     * StringUtils.countMatches("abba", "ab")  = 1
     * StringUtils.countMatches("abba", "xxx") = 0
     * </pre>
     * </p>
     *
     * @param str    要扫描的字符串
     * @param subStr 子字符串
     * @return 子串在字符串中出现的次数，如果字符串为<code>null</code>或空，则返回<code>0</code>
     */
    public static int countMatches(String str, String subStr) {
        if ((str == null) || (str.length() == 0) || (subStr == null) || (subStr.length() == 0)) {
            return 0;
        }

        int count = 0;
        int index = 0;

        while ((index = str.indexOf(subStr, index)) != -1) {
            count++;
            index += subStr.length();
        }

        return count;
    }

    /**
     * 系统打印字符串
     * @param str
     */
    public static void out(String str){
        System.out.println(str);
    }

}
