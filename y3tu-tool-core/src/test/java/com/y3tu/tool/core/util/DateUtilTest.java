package com.y3tu.tool.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/6/16
 */
public class DateUtilTest {

    @Test
    public void getDateToWeek() {
        String weekDay = DateUtil.getDateToWeek(new Date());
        Assert.assertEquals("星期日", weekDay);
    }
}