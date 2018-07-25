package com.y3tu.tool.core.text;


import com.y3tu.tool.core.annotation.Nullable;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author y3tu
 * @date 2018/2/27
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    public static final int INDEX_NOT_FOUND = -1;

    public static final char C_SPACE = CharUtil.SPACE;
    public static final char C_TAB = CharUtil.TAB;
    public static final char C_DOT = CharUtil.DOT;
    public static final char C_SLASH = CharUtil.SLASH;
    public static final char C_BACKSLASH = CharUtil.BACKSLASH;
    public static final char C_CR = CharUtil.CR;
    public static final char C_LF = CharUtil.LF;
    public static final char C_UNDERLINE = CharUtil.UNDERLINE;
    public static final char C_COMMA = CharUtil.COMMA;
    public static final char C_DELIM_START = CharUtil.DELIM_START;
    public static final char C_DELIM_END = CharUtil.DELIM_END;
    public static final char C_BRACKET_START = CharUtil.BRACKET_START;
    public static final char C_BRACKET_END = CharUtil.BRACKET_END;
    public static final char C_COLON = CharUtil.COLON;

    public static final String SPACE = " ";
    public static final String TAB = "	";
    public static final String DOT = ".";
    public static final String DOUBLE_DOT = "..";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String EMPTY = "";
    public static final String CR = "\r";
    public static final String LF = "\n";
    public static final String CRLF = "\r\n";
    public static final String UNDERLINE = "_";
    public static final String DASHED = "-";
    public static final String COMMA = ",";
    public static final String DELIM_START = "{";
    public static final String DELIM_END = "}";
    public static final String BRACKET_START = "[";
    public static final String BRACKET_END = "]";
    public static final String COLON = ":";

    public static final String HTML_NBSP = "&nbsp;";
    public static final String HTML_AMP = "&amp;";
    public static final String HTML_QUOTE = "&quot;";
    public static final String HTML_APOS = "&apos;";
    public static final String HTML_LT = "&lt;";
    public static final String HTML_GT = "&gt;";

    public static final String EMPTY_JSON = "{}";

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
     * 是否包含空字符串
     *
     * @param strs 字符串列表
     * @return 是否包含空字符串
     */
    public static boolean hasBlank(CharSequence... strs) {
        if (strs.length == 0) {
            return true;
        }

        for (CharSequence str : strs) {
            if (isBlank(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对象为空
     *
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
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param params   参数值
     * @return 格式化后的文本
     */
    public static String format(CharSequence template, Object... params) {
        if (null == template) {
            return null;
        }
        if (isBlank(template) || params.length == 0) {
            return template.toString();
        }
        return StrFormatter.format(template.toString(), params);
    }

    /**
     * 比较两个字符串（大小写敏感）。
     *
     * <pre>
     * equals(null, null)   = true
     * equals(null, &quot;abc&quot;)  = false
     * equals(&quot;abc&quot;, null)  = false
     * equals(&quot;abc&quot;, &quot;abc&quot;) = true
     * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equals(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, false);
    }

    /**
     * 比较两个字符串（大小写不敏感）。
     *
     * <pre>
     * equalsIgnoreCase(null, null)   = true
     * equalsIgnoreCase(null, &quot;abc&quot;)  = false
     * equalsIgnoreCase(&quot;abc&quot;, null)  = false
     * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
     * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
     * </pre>
     *
     * @param str1 要比较的字符串1
     * @param str2 要比较的字符串2
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     */
    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, true);
    }

    /**
     * 比较两个字符串是否相等。
     *
     * @param str1       要比较的字符串1
     * @param str2       要比较的字符串2
     * @param ignoreCase 是否忽略大小写
     * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
     * @since 3.2.0
     */
    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            // 只有两个都为null才判断相等
            return str2 == null;
        }
        if (null == str2) {
            // 字符串2空，字符串1非空，直接false
            return false;
        }

        if (ignoreCase) {
            return str1.toString().equalsIgnoreCase(str2.toString());
        } else {
            return str1.equals(str2);
        }
    }

    /**
     * 判断字符串是否以字母结尾
     * <p>
     * 如果字符串为Null或空，返回false
     */
    public static boolean endWith(@Nullable CharSequence s, char c) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        return s.charAt(s.length() - 1) == c;
    }

    /**
     * 如果结尾字符为c, 去除掉该字符.
     */
    public static String removeEnd(final String s, final char c) {
        if (endWith(s, c)) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }


    /**
     * 忽略大小写去掉指定前缀
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
     */
    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (isEmpty(str) || isEmpty(prefix)) {
            return str.toString();
        }

        final String str2 = str.toString();
        if (str2.toLowerCase().startsWith(prefix.toString().toLowerCase())) {
            // 截取后半段
            return substring(str2, prefix.length());
        }
        return str2;
    }

    /**
     * 字符串是否以给定字符开始
     *
     * @param str 字符串
     * @param c 字符
     * @return 是否开始
     */
    public static boolean startWith(CharSequence str, char c) {
        return c == str.charAt(0);
    }

    /**
     * 编码字符串
     *
     * @param str 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return 编码后的字节码
     */
    public static byte[] bytes(CharSequence str, Charset charset) {
        if (str == null) {
            return null;
        }

        if (null == charset) {
            return str.toString().getBytes();
        }
        return str.toString().getBytes(charset);
    }

}
