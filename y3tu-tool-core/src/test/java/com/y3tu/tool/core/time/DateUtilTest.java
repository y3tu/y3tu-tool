package com.y3tu.tool.core.time;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class DateUtilTest {

    @Test
    public void getDateToWeek() {
        Console.error(DateUtil.getDateToWeek(new Date()));
    }

    @Test
    public void getDayOfWeek() {
        Console.log(DateUtil.getDayOfWeek(new Date()));
    }
}