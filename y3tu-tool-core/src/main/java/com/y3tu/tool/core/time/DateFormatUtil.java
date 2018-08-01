package com.y3tu.tool.core.time;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.y3tu.tool.core.annotation.NotNull;
import com.y3tu.tool.core.annotation.Nullable;
import com.y3tu.tool.core.exception.UtilException;
import com.y3tu.tool.core.text.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;


/**
 * 时间格式转换工具
 *
 * @author y3tu
 */
public class DateFormatUtil {

    public static final TimeZone DEFAULT_UTC = TimeZone.getTimeZone("GMT+8");
    /**
     * 时间格式
     */
    public final static String PATTERN_YYYY = "yyyy";
    public final static String PATTERN_MM = "MM";
    public final static String PATTERN_DD = "dd";
    public final static String PATTERN_YYYY_MM = "yyyy-MM";
    public final static String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public final static String PATTERN_HH_MM_SS = "HH:mm:ss";
    public final static String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 带时区
     */
    public final static String PATTERN_YYY_MM_DD_T_HH_MM_SS_SSSZZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    public final static String PATTERN_YYY_MM_DD_T_HH_MM_SSZZ = "yyyy-MM-dd'T'HH:mm:ssZZ";
    /**
     * 带星期几
     */
    public final static String PATTERN_YYYY_MM_DD_HH_MM_SS_EE = "yyyy-MM-dd HH:mm:ss EE";
    /**
     * 中文格式
     */
    public final static String PATTERN_YYYY_MM_DD_HH_MM_SS_EE_ZH = "yyyy年MM月dd日 HH:mm:ss EE";


    /**
     * 使用工厂方法FastDateFormat.getInstance(), 从缓存中获取实例
     */
    public static final FastDateFormat YYYY_FORMAT = FastDateFormat.getInstance(PATTERN_YYYY, DEFAULT_UTC);
    public static final FastDateFormat MM_FORMAT = FastDateFormat.getInstance(PATTERN_MM, DEFAULT_UTC);
    public static final FastDateFormat DD_FORMAT = FastDateFormat.getInstance(PATTERN_DD);
    public static final FastDateFormat YYYY_MM_DD_FORMAT = FastDateFormat.getInstance(PATTERN_YYYY_MM_DD, DEFAULT_UTC);
    public static final FastDateFormat YYYY_MM_FORMAT = FastDateFormat.getInstance(PATTERN_YYYY_MM, DEFAULT_UTC);
    public static final FastDateFormat HH_MM_SS_FORMAT = FastDateFormat.getInstance(PATTERN_HH_MM_SS, DEFAULT_UTC);
    public static final FastDateFormat YYYY_MM_DD_HH_MM_SS_FORMAT = FastDateFormat.getInstance(PATTERN_YYYY_MM_DD_HH_MM_SS, DEFAULT_UTC);
    public static final FastDateFormat YYY_MM_DD_T_HH_MM_SS_SSSZZ_FORMAT = FastDateFormat.getInstance(PATTERN_YYY_MM_DD_T_HH_MM_SS_SSSZZ, DEFAULT_UTC);
    public static final FastDateFormat YYY_MM_DD_T_HH_MM_SSZZ_FORMAT = FastDateFormat.getInstance(PATTERN_YYY_MM_DD_T_HH_MM_SSZZ, DEFAULT_UTC);


    /**
     * 转换时间格式 把时间字符串转换为Date类型
     *
     * @param pattern    时间格式
     * @param dateString 时间字符串
     * @return
     */
    public static Date parseDate(String pattern, String dateString) {
        return parseDate(pattern, dateString, null);
    }

    /**
     * 分析日期字符串, 仅用于pattern不固定的情况.
     * <p>
     * 否则直接使用本类中封装好的FastDateFormat.
     * <p>
     * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
     *
     * @param pattern    时间格式
     * @param dateString 时间字符串
     * @param timeZone   国际时区
     */
    public static Date parseDate(@NotNull String pattern, @NotNull String dateString, TimeZone timeZone) {
        try {
            if (timeZone == null) {
                timeZone = DEFAULT_UTC;
            }
            return FastDateFormat.getInstance(pattern, timeZone).parse(dateString);
        } catch (ParseException e) {
            throw new UtilException(e);
        }
    }

    /**
     * 转换时间格式 把Date类型转换成字符串
     *
     * @param pattern
     * @param date
     * @return
     */
    public static String formatDate(@NotNull String pattern, @NotNull Date date) {
        return formatDate(pattern, date, null);
    }

    /**
     * 格式化日期, 仅用于pattern不固定的情况.
     * <p>
     * 否则直接使用本类中封装好的FastDateFormat.
     * <p>
     * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
     *
     * @param pattern  时间格式
     * @param date     时间
     * @param timeZone 国际时区
     */
    public static String formatDate(@NotNull String pattern, @NotNull Date date, TimeZone timeZone) {
        if (timeZone == null) {
            timeZone = DEFAULT_UTC;
        }
        return FastDateFormat.getInstance(pattern, timeZone).format(date);
    }

    /**
     * 转换时间格式
     *
     * @param pattern 时间格式
     * @param date    时间
     * @return
     */
    public static Date formatDateToDate(String pattern, Date date) throws ParseException {
        return DateFormatUtil.parseDate(pattern, DateFormatUtil.formatDate(pattern, date));
    }

    /**
     * 转换时间格式
     *
     * @param pattern  时间格式
     * @param date     时间
     * @param timeZone 国际时区
     * @return
     */
    public static Date formatDateToDate(String pattern, Date date, @Nullable TimeZone timeZone) {
        return DateFormatUtil.parseDate(pattern, DateFormatUtil.formatDate(pattern, date, timeZone), timeZone);
    }

    /**
     * 转换时间格式
     *
     * @param pattern 时间格式
     * @param dateStr 时间
     * @return
     */
    public static String formatStrToStr(String pattern, String dateStr) {
        return formatStrToStr(pattern, dateStr, null);
    }

    /**
     * 转换时间格式
     *
     * @param pattern  时间格式
     * @param dateStr  时间
     * @param timeZone 国际时区
     * @return
     */
    public static String formatStrToStr(String pattern, String dateStr, @Nullable TimeZone timeZone) {
        return DateFormatUtil.formatDate(pattern, DateFormatUtil.parseDate(pattern, dateStr, timeZone), timeZone);
    }

    /**
     * 转换时间格式
     *
     * @param pattern 时间格式
     * @param date    时间量
     * @return
     */
    public static String formatDate(@NotNull String pattern, long date) {
        return formatDate(pattern, date);
    }

    /**
     * 格式化日期, 仅用于不固定pattern不固定的情况.
     * <p>
     * 否否则直接使用本类中封装好的FastDateFormat.
     * <p>
     * FastDateFormat.getInstance()已经做了缓存，不会每次创建对象，但直接使用对象仍然能减少在缓存中的查找.
     *
     * @param pattern  时间格式
     * @param date     时间量
     * @param timeZone 国际时区
     */
    public static String formatDate(@NotNull String pattern, long date, TimeZone timeZone) {
        if (timeZone == null) {
            timeZone = DEFAULT_UTC;
        }
        return FastDateFormat.getInstance(pattern, timeZone).format(date);
    }


    /**
     * 按照指定格式将字符串日期转换为SQL需要的日期对象
     *
     * @param strDate String，欲转换的字符串型日期
     * @param pattern String，指定的字符串日期格式
     * @return java.sql.Date 转换后的java.sql.Date型日期
     */
    public static java.sql.Date formatToSqlDate(String pattern, String strDate) {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }
        return new java.sql.Date(parseDate(pattern, strDate).getTime());
    }

    //-----------------------------打印用于页面显示的用户友好，与当前时间比的时间差

    /**
     * 打印用户友好的，与当前时间相比的时间差，如刚刚，5分钟前，今天XXX，昨天XXX
     * <p>
     * copy from AndroidUtilCode
     */
    public static String formatFriendlyTimeSpanByNow(@NotNull Date date) {
        return formatFriendlyTimeSpanByNow(date.getTime());
    }

    /**
     * 打印用户友好的，与当前时间相比的时间差，如刚刚，5分钟前，今天XXX，昨天XXX
     * <p>
     * copy from AndroidUtilCode
     */
    public static String formatFriendlyTimeSpanByNow(long timeStampMillis) {
        long now = ClockUtil.currentTimeMillis();
        long span = now - timeStampMillis;
        if (span < 0) {
            // 'c' 日期和时间，被格式化为 "%ta %tb %td %tT %tZ %tY"，例如 "Sun Jul 20 16:17:00 EDT 1969"。
            return String.format("%tc", timeStampMillis);
        }
        if (span < DateUtil.MILLIS_PER_SECOND) {
            return "刚刚";
        } else if (span < DateUtil.MILLIS_PER_MINUTE) {
            return String.format("%d秒前", span / DateUtil.MILLIS_PER_SECOND);
        } else if (span < DateUtil.MILLIS_PER_HOUR) {
            return String.format("%d分钟前", span / DateUtil.MILLIS_PER_MINUTE);
        }
        // 获取当天00:00
        long wee = DateUtil.beginOfDate(new Date(now)).getTime();
        if (timeStampMillis >= wee) {
            // 'R' 24 小时制的时间，被格式化为 "%tH:%tM"
            return String.format("今天%tR", timeStampMillis);
        } else if (timeStampMillis >= wee - DateUtil.MILLIS_PER_DAY) {
            return String.format("昨天%tR", timeStampMillis);
        } else {
            // 'F' ISO 8601 格式的完整日期，被格式化为 "%tY-%tm-%td"。
            return String.format("%tF", timeStampMillis);
        }
    }
}
