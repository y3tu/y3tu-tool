package com.y3tu.tool.core.time;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/7/24
 */
public class DateFormatUtilTest {

    @Test
    public void formatFriendlyTimeSpanByNow() throws ParseException {
        Date date = DateFormatUtil.YYYY_MM_DD_HH_MM_SS_FORMAT.parse("2018-07-23 23:29:59.500");
        Console.log(DateFormatUtil.formatFriendlyTimeSpanByNow(date));

    }

    @Test
    public void formatDuration() {

    }
}