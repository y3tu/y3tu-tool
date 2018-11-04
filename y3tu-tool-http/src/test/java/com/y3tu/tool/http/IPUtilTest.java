package com.y3tu.tool.http;

import cn.hutool.core.lang.Console;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/10/31
 */
public class IPUtilTest {

    @Test
    public void getCityInfo() {
        Console.log(IPUtil.getCityInfo("117.136.75.178"));
    }
}