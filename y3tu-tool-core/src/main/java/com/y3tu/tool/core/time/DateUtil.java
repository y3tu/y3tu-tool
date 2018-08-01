package com.y3tu.tool.core.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import com.y3tu.tool.core.annotation.NotNull;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateUtils;


/**
 * 日期工具类.
 *
 * @author y3tu
 */
public class DateUtil {

    /**
     * Number of milliseconds in a standard second.
     */
    public static final long MILLIS_PER_SECOND = 1000;
    /**
     * Number of milliseconds in a standard minute.
     */
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    /**
     * Number of milliseconds in a standard hour.
     */
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    /**
     * Number of milliseconds in a standard day.
     */
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    /**
     * 一年中每个月的天数
     */
    private static final int[] MONTH_LENGTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


    //----------------------获取当前时间

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期
     *
     * @return 返回类型 Date 返回类型 一个包含年月日的<code>Date</code>型日期
     */
    public static synchronized Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 获取当前日期戳
     *
     * @return String 日期戳字符串
     */
    public static String getCurrentDateStr() {
        return DateFormatUtil.YYYY_MM_DD_HH_MM_SS_FORMAT.format(getNowDate());
    }

    /**
     * 获取当前日期戳
     *
     * @param pattern 时间格式
     * @return String 日期戳字符串
     */
    public static String getCurrentDateStr(String pattern) {
        return DateFormatUtil.formatDate(pattern, getNowDate());
    }


    //---------------------------------- 日期比较

    /**
     * 判断是否是日期
     *
     * @param dateStr 日期字符串
     * @return
     */
    public static boolean checkDate(String dateStr) {
        String regex = "[1-2]{1}\\d{3}[0-1]{1}\\d{1}[0-3]{1}\\d{1}";
        return Pattern.matches(regex, dateStr);
    }


    /**
     * 是否同一天.
     *
     * @param date1 时间1
     * @param date2 时间2
     * @see DateUtils#isSameDay(Date, Date)
     */
    public static boolean isSameDay(@NotNull final Date date1, @NotNull final Date date2) {
        return DateUtils.isSameDay(date1, date2);
    }

    /**
     * 是否同一时刻.
     *
     * @param date1 时间1
     * @param date2 时间2
     */
    public static boolean isSameTime(@NotNull final Date date1, @NotNull final Date date2) {
        // date.getMillisOf() 比date.getTime()快
        return date1.compareTo(date2) == 0;
    }


    /**
     * 判断日期是否在范围内，包含相等的日期
     *
     * @param date  要判断的时间
     * @param start 区间开始时间
     * @param end   区间结束时间
     */
    public static boolean isBetween(@NotNull final Date date, @NotNull final Date start, @NotNull final Date end) {
        if (date == null || start == null || end == null || start.after(end)) {
            throw new IllegalArgumentException("some date parameters is null or dateBein after dateEnd");
        }
        return !date.before(start) && !date.after(end);
    }

    /**
     * 取得两个时间段的时间间隔
     *
     * @param t1 时间1
     * @param t2 时间2
     * @return t2 与t1的间隔年数
     * @throws ParseException 如果输入的日期格式不是0000-00-00 格式抛出异常
     * @author color
     */
    public static int getBetweenYears(String t1, String t2) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = format.parse(t1);
        Date d2 = format.parse(t2);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        // 保证第二个时间一定大于第一个时间
        if (c1.after(c2)) {
            c1 = c2;
            c2.setTime(d1);
        }
        int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

        return betweenYears;
    }

    /**
     * 取得两个时间段的时间间隔
     *
     * @param t1 时间1
     * @param t2 时间2
     * @return t2 与t1的间隔天数
     * @throws ParseException 如果输入的日期格式不是0000-00-00 格式抛出异常
     * @author color
     */
    public static int getBetweenDays(String t1, String t2) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        int betweenDays = 0;
        Date d1 = format.parse(t1);
        Date d2 = format.parse(t2);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        // 保证第二个时间一定大于第一个时间
        if (c1.after(c2)) {
            c1 = c2;
            c2.setTime(d1);
        }
        int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        betweenDays = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
        for (int i = 0; i < betweenYears; i++) {
            c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
            betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
        }
        return betweenDays;
    }


    //------------------------------- 时间偏移

    /**
     * 加一月
     */
    public static Date addMonths(@NotNull final Date date, int amount) {
        return DateUtils.addMonths(date, amount);
    }

    /**
     * 减一月
     */
    public static Date subMonths(@NotNull final Date date, int amount) {
        return DateUtils.addMonths(date, -amount);
    }

    /**
     * 加一周
     */
    public static Date addWeeks(@NotNull final Date date, int amount) {
        return DateUtils.addWeeks(date, amount);
    }

    /**
     * 减一周
     */
    public static Date subWeeks(@NotNull final Date date, int amount) {
        return DateUtils.addWeeks(date, -amount);
    }

    /**
     * 加一天
     */
    public static Date addDays(@NotNull final Date date, final int amount) {
        return DateUtils.addDays(date, amount);
    }

    /**
     * 减一天
     */
    public static Date subDays(@NotNull final Date date, int amount) {
        return DateUtils.addDays(date, -amount);
    }

    /**
     * 加一小时
     */
    public static Date addHours(@NotNull final Date date, int amount) {
        return DateUtils.addHours(date, amount);
    }

    /**
     * 减一小时
     */
    public static Date subHours(@NotNull final Date date, int amount) {
        return DateUtils.addHours(date, -amount);
    }

    /**
     * 加一分钟
     */
    public static Date addMinutes(@NotNull final Date date, int amount) {
        return DateUtils.addMinutes(date, amount);
    }

    /**
     * 减一分钟
     */
    public static Date subMinutes(@NotNull final Date date, int amount) {
        return DateUtils.addMinutes(date, -amount);
    }

    /**
     * 终于到了，续一秒.
     */
    public static Date addSeconds(@NotNull final Date date, int amount) {
        return DateUtils.addSeconds(date, amount);
    }

    /**
     * 减一秒.
     */
    public static Date subSeconds(@NotNull final Date date, int amount) {
        return DateUtils.addSeconds(date, -amount);
    }

    //--------------------------------直接设置时间

    /**
     * 设置年份, 公元纪年.
     */
    public static Date setYears(@NotNull final Date date, int amount) {
        return DateUtils.setYears(date, amount);
    }

    /**
     * 设置月份, 0-11.
     */
    public static Date setMonths(@NotNull final Date date, int amount) {
        return DateUtils.setMonths(date, amount);
    }

    /**
     * 设置日期, 1-31.
     */
    public static Date setDays(@NotNull final Date date, int amount) {
        return DateUtils.setDays(date, amount);
    }

    /**
     * 设置小时, 0-23.
     */
    public static Date setHours(@NotNull final Date date, int amount) {
        return DateUtils.setHours(date, amount);
    }

    /**
     * 设置分钟, 0-59.
     */
    public static Date setMinutes(@NotNull final Date date, int amount) {
        return DateUtils.setMinutes(date, amount);
    }

    /**
     * 设置秒, 0-59.
     */
    public static Date setSeconds(@NotNull final Date date, int amount) {
        return DateUtils.setSeconds(date, amount);
    }

    /**
     * 设置毫秒.
     */
    public static Date setMilliseconds(@NotNull final Date date, int amount) {
        return DateUtils.setMilliseconds(date, amount);
    }

    //---------------------------------- 获取日期的位置

    /**
     * 获得日期是一周的第几天. 已改为中国习惯，1 是Monday，而不是Sundays.
     *
     * @param date 时间
     */
    public static int getDayOfWeek(@NotNull final Date date) {
        int result = getWithMondayFirst(date, Calendar.DAY_OF_WEEK);
        return result == 1 ? 7 : result - 1;
    }

    /**
     * 判断今天是不是周末
     *
     * @return true/false
     */
    public static boolean isTodayWeekend() {

        // 获取当前日期
        Calendar c = Calendar.getInstance();
        // 获取当前日期星期，英国算法(周日为一周第一天)
        int day = c.get(Calendar.DAY_OF_WEEK);
        // 如果是周六或周日就返回true
        if (day == 7 || day == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前时间是星期几
     *
     * @param date 时间
     * @return
     */
    public static String getDateToWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String week = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }
        return week;
    }

    /**
     * 获得日期是一年的第几天，返回值从1开始
     *
     * @param date 时间
     */
    public static int getDayOfYear(@NotNull final Date date) {
        return get(date, Calendar.DAY_OF_YEAR);
    }

    /**
     * 获得日期是一月的第几周，返回值从1开始.
     * <p>
     * 开始的一周，只要有一天在那个月里都算. 已改为中国习惯，1 是Monday，而不是Sunday
     *
     * @param date 时间
     */
    public static int getWeekOfMonth(@NotNull final Date date) {
        return getWithMondayFirst(date, Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获得日期是一年的第几周，返回值从1开始.
     * <p>
     * 开始的一周，只要有一天在那一年里都算.已改为中国习惯，1 是Monday，而不是Sunday
     *
     * @param date 时间
     */
    public static int getWeekOfYear(@NotNull final Date date) {
        return getWithMondayFirst(date, Calendar.WEEK_OF_YEAR);
    }

    private static int get(final Date date, int field) {
        Validate.notNull(date, "The date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(field);
    }

    private static int getWithMondayFirst(final Date date, int field) {
        Validate.notNull(date, "The date must not be null");
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        return cal.get(field);
    }

    //----------------------获得往前往后的日期

    /**
     * 2016-11-10 07:33:23, 则返回2016-1-1 00:00:00
     */
    public static Date beginOfYear(@NotNull final Date date) {
        return DateUtils.truncate(date, Calendar.YEAR);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-12-31 23:59:59.999
     */
    public static Date endOfYear(@NotNull final Date date) {
        return new Date(nextYear(date).getTime() - 1);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2017-1-1 00:00:00
     */
    public static Date nextYear(@NotNull final Date date) {
        return DateUtils.ceiling(date, Calendar.YEAR);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-11-1 00:00:00
     */
    public static Date beginOfMonth(@NotNull final Date date) {
        return DateUtils.truncate(date, Calendar.MONTH);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-11-30 23:59:59.999
     */
    public static Date endOfMonth(@NotNull final Date date) {
        return new Date(nextMonth(date).getTime() - 1);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-12-1 00:00:00
     */
    public static Date nextMonth(@NotNull final Date date) {
        return DateUtils.ceiling(date, Calendar.MONTH);
    }

    /**
     * 2017-1-20 07:33:23, 则返回2017-1-16 00:00:00
     */
    public static Date beginOfWeek(@NotNull final Date date) {
        return DateUtils.truncate(DateUtil.subDays(date, DateUtil.getDayOfWeek(date) - 1), Calendar.DATE);
    }

    /**
     * 2017-1-20 07:33:23, 则返回2017-1-22 23:59:59.999
     */
    public static Date endOfWeek(@NotNull final Date date) {
        return new Date(nextWeek(date).getTime() - 1);
    }

    /**
     * 2017-1-23 07:33:23, 则返回2017-1-22 00:00:00
     */
    public static Date nextWeek(@NotNull final Date date) {
        return DateUtils.truncate(DateUtil.addDays(date, 8 - DateUtil.getDayOfWeek(date)), Calendar.DATE);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-11-10 00:00:00
     */
    public static Date beginOfDate(@NotNull final Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

    /**
     * 2017-1-23 07:33:23, 则返回2017-1-23 23:59:59.999
     */
    public static Date endOfDate(@NotNull final Date date) {
        return new Date(nextDate(date).getTime() - 1);
    }

    /**
     * 2016-11-10 07:33:23, 则返回2016-11-11 00:00:00
     */
    public static Date nextDate(@NotNull final Date date) {
        return DateUtils.ceiling(date, Calendar.DATE);
    }

    /**
     * 2016-12-10 07:33:23, 则返回2016-12-10 07:00:00
     */
    public static Date beginOfHour(@NotNull final Date date) {
        return DateUtils.truncate(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 2017-1-23 07:33:23, 则返回2017-1-23 07:59:59.999
     */
    public static Date endOfHour(@NotNull final Date date) {
        return new Date(nextHour(date).getTime() - 1);
    }

    /**
     * 2016-12-10 07:33:23, 则返回2016-12-10 08:00:00
     */
    public static Date nextHour(@NotNull final Date date) {
        return DateUtils.ceiling(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 2016-12-10 07:33:23, 则返回2016-12-10 07:33:00
     */
    public static Date beginOfMinute(@NotNull final Date date) {
        return DateUtils.truncate(date, Calendar.MINUTE);
    }

    /**
     * 2017-1-23 07:33:23, 则返回2017-1-23 07:33:59.999
     */
    public static Date endOfMinute(@NotNull final Date date) {
        return new Date(nextMinute(date).getTime() - 1);
    }

    /**
     * 2016-12-10 07:33:23, 则返回2016-12-10 07:34:00
     */
    public static Date nextMinute(@NotNull final Date date) {
        return DateUtils.ceiling(date, Calendar.MINUTE);
    }

    //------------------------------闰年及每月天数------------------------

    /**
     * 是否闰年.
     */
    public static boolean isLeapYear(@NotNull final Date date) {
        return isLeapYear(get(date, Calendar.YEAR));
    }

    /**
     * 是否闰年
     * <p>
     *
     * @param y 参数是公元计数, 如2016
     */
    public static boolean isLeapYear(int y) {
        boolean result = false;
        boolean flag = ((y % 4) == 0) && ((y < 1582) || ((y % 100) != 0) || ((y % 400) == 0));
        if (flag) {
            result = true;
        }
        return result;
    }

    /**
     * 获取某个月有多少天, 考虑闰年等因数, 移植Jodd Core的TimeUtil
     */
    public static int getMonthLength(@NotNull final Date date) {
        int year = get(date, Calendar.YEAR);
        int month = get(date, Calendar.MONTH);
        return getMonthLength(year, month);
    }

    /**
     * 获取某个月有多少天, 考虑闰年等因数, 移植Jodd Core的TimeUtil
     */
    public static int getMonthLength(int year, int month) {

        if ((month < 1) || (month > 12)) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }
        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        }

        return MONTH_LENGTH[month];
    }


}
