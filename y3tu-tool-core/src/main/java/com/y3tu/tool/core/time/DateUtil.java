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
 * <p>
 * 在不方便使用joda-time时，使用本类降低Date处理的复杂度与性能消耗, 封装Common Lang及移植Jodd的最常用日期方法
 */
public class DateUtil {

    public static final long MILLIS_PER_SECOND = 1000; // Number of milliseconds in a standard second.
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND; // Number of milliseconds in a standard minute.
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE; // Number of milliseconds in a standard hour.
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR; // Number of milliseconds in a standard day.

    private static final int[] MONTH_LENGTH = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};


    ////////获取当前时间///////////

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
     * @return 返回当前日期(yyyyMMdd)
     */
    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 获取当前日期
     *
     * @return 返回当前日期(yyyyMMdd HH : mm : ss)
     */
    public static String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(calendar.getTime());
    }

    /**
     * 按特定的日期格式获取当前字符串型日期
     *
     * @param dateFormatType String，日期格式<br>
     *                       几种日期格式和测试的结果<br>
     *                       "yyyy-MM-dd": 2012-08-02<br>
     *                       "yyyy-MM-dd hh:mm:ss": 2012-08-02 11:27:41<br>
     *                       "yyyy-MM-dd hh:mm:ss EE": 2012-08-02 11:27:41 星期四<br>
     *                       "yyyy年MM月dd日 hh:mm:ss EE": 2012年08月02日 11:27:41 星期四<br>
     * @return String 当前字符串型日期
     */
    public static String getNowDateFormat(String dateFormatType) {
        SimpleDateFormat simformat = new SimpleDateFormat(dateFormatType);
        return simformat.format(getNowDate());
    }

    /**
     * 按特定的日期格式获取当前字符串型日期
     *
     * @param date           指定的时间
     * @param dateFormatType String，日期格式<br>
     *                       几种日期格式和测试的结果<br>
     *                       "yyyy-MM-dd": 2012-08-02<br>
     *                       "yyyy-MM-dd hh:mm:ss": 2012-08-02 11:27:41<br>
     *                       "yyyy-MM-dd hh:mm:ss EE": 2012-08-02 11:27:41 星期四<br>
     *                       "yyyy年MM月dd日 hh:mm:ss EE": 2012年08月02日 11:27:41 星期四<br>
     * @return String 当前字符串型日期
     */
    public static String getDateFormat(Date date, String dateFormatType) {
        SimpleDateFormat simformat = new SimpleDateFormat(dateFormatType);
        return simformat.format(date);
    }


    //////// 日期比较 ///////////

    /**
     * 是否同一天.
     *
     * @see DateUtils#isSameDay(Date, Date)
     */
    public static boolean isSameDay(@NotNull final Date date1, @NotNull final Date date2) {
        return DateUtils.isSameDay(date1, date2);
    }

    /**
     * 是否同一时刻.
     */
    public static boolean isSameTime(@NotNull final Date date1, @NotNull final Date date2) {
        // date.getMillisOf() 比date.getTime()快
        return date1.compareTo(date2) == 0;
    }


    /**
     * 判断日期是否在范围内，包含相等的日期
     */
    public static boolean isBetween(@NotNull final Date date, @NotNull final Date start, @NotNull final Date end) {
        if (date == null || start == null || end == null || start.after(end)) {
            throw new IllegalArgumentException("some date parameters is null or dateBein after dateEnd");
        }
        return !date.before(start) && !date.after(end);
    }

    //////////// 往前往后滚动时间//////////////

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

    //////////// 直接设置时间//////////////

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

    ///// 获取日期的位置//////

    /**
     * 获得日期是一周的第几天. 已改为中国习惯，1 是Monday，而不是Sundays.
     */
    public static int getDayOfWeek(@NotNull final Date date) {
        int result = getWithMondayFirst(date, Calendar.DAY_OF_WEEK);
        return result == 1 ? 7 : result - 1;
    }

    /**
     * 获取当前时间是星期几
     *
     * @param date
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
     */
    public static int getDayOfYear(@NotNull final Date date) {
        return get(date, Calendar.DAY_OF_YEAR);
    }

    /**
     * 获得日期是一月的第几周，返回值从1开始.
     * <p>
     * 开始的一周，只要有一天在那个月里都算. 已改为中国习惯，1 是Monday，而不是Sunday
     */
    public static int getWeekOfMonth(@NotNull final Date date) {
        return getWithMondayFirst(date, Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获得日期是一年的第几周，返回值从1开始.
     * <p>
     * 开始的一周，只要有一天在那一年里都算.已改为中国习惯，1 是Monday，而不是Sunday
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

    ///// 获得往前往后的日期//////

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

    ////// 闰年及每月天数///////

    /**
     * 是否闰年.
     */
    public static boolean isLeapYear(@NotNull final Date date) {
        return isLeapYear(get(date, Calendar.YEAR));
    }

    /**
     * 是否闰年，copy from Jodd Core的TimeUtil
     * <p>
     * 参数是公元计数, 如2016
     */
    public static boolean isLeapYear(int y) {
        boolean result = false;

        if (((y % 4) == 0) && // must be divisible by 4...
                ((y < 1582) || // and either before reform year...
                        ((y % 100) != 0) || // or not a century...
                        ((y % 400) == 0))) { // or a multiple of 400...
            result = true; // for leap year.
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

    /**
     * 判断今天是不是周末
     *
     * @return true/false
     */
    public static boolean isTodayWeekend() {

        Calendar c = Calendar.getInstance(); // 获取当前日期
        int day = c.get(Calendar.DAY_OF_WEEK); // 获取当前日期星期，英国算法(周日为一周第一天)
        if (day == 7 || day == 1) { // 如果是周六或周日就返回true
            return true;
        }
        return false;
    }

    /**
     * 按照指定格式将字符串日期转换为SQL需要的日期对象
     *
     * @param strDate         String，欲转换的字符串型日期
     * @param dateFormateType String，指定的字符串日期格式
     * @return java.sql.Date 转换后的java.sql.Date型日期
     */
    public static java.sql.Date turnDateToSqlDate(String strDate, String dateFormateType) {
        if (strDate == null) {
            return null;
        }
        if (strDate.trim().equals("")) {
            return null;
        }

        return new java.sql.Date(turnStrDateToJavaUtilDate(strDate, dateFormateType).getTime());
    }


    /**
     * 按指定的字符串格式将字符串型日期转化为Date型日期
     *
     * @param dateFormatType "yyyy-MM-dd" 或者 "yyyy-MM-dd hh:mm:ss"
     * @return Date型日期
     * @throws Exception
     * @Param dateStr 字符型日期
     */
    public static Date turnStrDateToJavaUtilDate(String strDate, String dateFormatType) {

        Date javaUtilDate = null;
        DateFormat df = new SimpleDateFormat(dateFormatType);
        if (strDate != null && (!"".equals(strDate)) && dateFormatType != null && (!"".equals(dateFormatType))) {

            try {

                javaUtilDate = df.parse(strDate);
            } catch (ParseException e) {

            }
        }
        return javaUtilDate;
    }

    /**
     * 将Date型日期转化指定格式的字符串型日期
     *
     * @param javaUtilDate   Date,传入的Date型日期
     * @param dateFormatType "yyyy-MM-dd"或者<br>
     *                       "yyyy-MM-dd hh:mm:ss EE"或者<br>
     *                       "yyyy年MM月dd日 hh:mm:ss EE" <br>
     *                       (年月日 时:分:秒 星期 ，注意MM/mm大小写)
     * @return String 指定格式的字符串型日期
     */
    public static String turnJavaUtilDateToStrDate(Date javaUtilDate, String dateFormatType) {

        String strDate = "";
        if (javaUtilDate != null) {

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatType);
            strDate = sdf.format(javaUtilDate);
        }
        return strDate;
    }

    /**
     * 获取当年指定月份第一天的字符串日期
     *
     * @param specifiedMonth 指定月份
     * @param dateFormatType 日期格式
     * @return String 指定月份第一天的字符串日期
     * @throws Exception CSExceptionCode.EC_2000,CSExceptionCode.MSG_2000
     */
    public static String getTheFirstDayOfSpecifiedMonth(int specifiedMonth, String dateFormatType) throws Exception {

        Date currentJavaUtilDate = getNowDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentJavaUtilDate);
        cal.set(Calendar.MONTH, specifiedMonth - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Date resultDate = cal.getTime();
        String result = turnJavaUtilDateToStrDate(resultDate, dateFormatType);
        return result;
    }

    /**
     * 获取当年指定月份的最后一天字符串日期
     *
     * @param specifiedMonth 指定月份
     * @param dateFormatType 日期格式
     * @return String 时间字符串
     * @throws Exception CSExceptionCode.EC_2000,CSExceptionCode.MSG_2000
     */
    public static String getTheLastDayOfSpecifiedMonth(int specifiedMonth, String dateFormatType) throws Exception {

        Date date = null;
        date = turnStrDateToJavaUtilDate(getTheFirstDayOfSpecifiedMonth(specifiedMonth, dateFormatType),
                dateFormatType);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String result = turnJavaUtilDateToStrDate(calendar.getTime(), "yyyy-MM-dd");
        return result;
    }

    /**
     * 获取当前月第一天的字符串日期
     *
     * @return String 当前月第一天的字符串日期。例如：当前日期是2012-08-2，则返回值为2012-08-1
     */
    public static String getTheFirstDayOfCurrentMonth() {

        Calendar cal = Calendar.getInstance();// 获取当前日期
        cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,即是本月第一天
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 格式化
        return df.format(cal.getTime());
    }

    /**
     * 获取当前月最后一天的字符串日期
     *
     * @return String 当前月最后一天的日期。 例如：当前日期是2012-08-2，则返回值为2012-08-31
     */
    public static String getTheLastDayOfCurrentMonth() {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//
        Calendar cal = Calendar.getInstance();// 获取当前日期，例如2012-08-02
        cal.set(Calendar.DAY_OF_MONTH, 1);// 转变为当前月的第一天，例如2012-08-01
        cal.add(Calendar.MONTH, 1);// 转变为下个月的第一天，例如2012-09-01
        cal.add(Calendar.DAY_OF_MONTH, -1);// 下个月第一天，倒数一天，即为当前月的最后一天。例如2012-08-31
        return df.format(cal.getTime());
    }

    /**
     * 验证传入日期是否为当前月最后一天
     *
     * @param targetObj 传入日期可为字符串、Date
     * @param formtStr  yyyy-MM-dd hh:mm:ss
     * @return true/false
     */
    public static boolean isTheLastDayOfCurrentMonth(Object targetObj, String formtStr) {

        boolean flag = false;
        if (targetObj == null) {// 如果传入日期参数为null，则返回false
            return flag;
        } else if ("".equals(targetObj.toString())) {// 如果传入日期参数为空字符串，则返回false
            return flag;
        }

        String srcDate = "";
        if (targetObj.getClass() == String.class) {
            srcDate = DateUtil.turnJavaUtilDateToStrDate(
                    DateUtil.turnStrDateToJavaUtilDate(targetObj.toString(), "yyyy-MM-dd"), formtStr);
        } else if (targetObj.getClass() == Date.class) {
            srcDate = DateUtil.turnJavaUtilDateToStrDate((Date) targetObj, formtStr);
        } else {
            srcDate = DateUtil.turnJavaUtilDateToStrDate(getNowDate(), "yyyy-MM-dd");
        }
        if (srcDate.compareTo(DateUtil.getTheLastDayOfCurrentMonth()) == 0) {// 和当前月最后一天比较的结果为0，则是当前月最后一天
            flag = true;
            return flag;
        } else {
            return flag;
        }
    }

    /**
     * 获取当前时间16位字符串 小富修改为时间16位+4位随机数2012091811320043154
     *
     * @return String e.g."2012082110290016"
     */
    public static String getCurrentDateTime16Str() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");

        /* 生成随机数 */
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        for (int i = 10; i > 1; i--) {
            int index = rand.nextInt(i);
            int tmp = array[index];
            array[index] = array[i - 1];
            array[i - 1] = tmp;
        }
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = result * 10 + array[i];
        }

        return sdf.format(getNowDate()) + result;
    }

    /**
     * 指定日期、相加月数值、格式，得到相加日期 例如：2011-06-19 2 yyyy-MM-dd 结果：2011-08-19 2011-06-19
     * 12 yyyy-MM-dd 结果：2012-06-19
     *
     * @param date     指定日期
     * @param formtStr 格式
     * @param number   数组
     * @param calender 指定修改日期格式数组
     * @return
     * @author leiyunshi
     */
    public static String tragetDate(String date, String formtStr, int number, int calender) {
        if (date == null) {
            return null;
        }

        if (date.trim().equals("")) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(formtStr);// "yyyy-MM-dd"

        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtil.toDate(date));
        cal.add(calender, number);
        // System.out.println(df.format(cal.getTime()));
        return df.format(cal.getTime());
    }

    /**
     * 按照"yyyy-MM-dd"格式将字符串date转换为日期对象
     *
     * @param date
     * @return
     */
    public static Date toDate(String date) {
        if (date == null) {
            return null;
        }
        if (date.trim().equals("")) {
            return null;
        }
        SimpleDateFormat simformat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simformat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按照"yyyy-MM-dd"格式将字符串date转换为日期对象
     *
     * @param date
     * @return
     */
    public static Date toDate(String date, String format) {
        if (date == null) {
            return null;
        }
        if (date.trim().equals("")) {
            return null;
        }
        SimpleDateFormat simformat = new SimpleDateFormat(format);
        try {
            return simformat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化CST（ Thu Aug 27 18:05:49 CST 2015 ）格式字符串
     *
     * @param date
     * @param format
     * @return
     * @throws ParseException
     */
    public static String formatCSTTime(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date d = sdf.parse(date);
        return DateUtil.getDateFormat(d, format);
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

    public static int getBetweenDays(String t1, String t2, String formatstr) {
        DateFormat format = new SimpleDateFormat(formatstr);
        int betweenDays = 0;
        Date d1, d2;
        Calendar c1, c2;
        try {
            d1 = format.parse(t1);
            d2 = format.parse(t2);
            c1 = Calendar.getInstance();
            c2 = Calendar.getInstance();
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
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return betweenDays;
    }

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
     * 获取当前时间戳
     *
     * @return String 时间戳字符串
     */
    public static String getCurrentStringTimestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(getNowDate());
    }

    /**
     * 获取当前日期戳
     *
     * @return String 日期戳字符串
     */
    public static String getCurrentStringDate() {
        return new SimpleDateFormat("yyyyMMdd").format(getNowDate());
    }

    /**
     * 获取当前月份的最后一天
     *
     * @return String 日期戳字符串
     */
    public static Integer getDaycountInMonth() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat format = new SimpleDateFormat("dd");
        String last = format.format(ca.getTime());
        return Integer.parseInt(last);
    }

    /**
     * 获取指定日期的前几天或者后几天
     *
     * @return String 日期戳字符串
     */
    public static String getCoupleDate(Date date, int day, String formtStr) {
        DateFormat format = new SimpleDateFormat(formtStr);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, day);
        return format.format(c.getTime());
    }


}
