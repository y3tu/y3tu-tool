package com.y3tu.tool.http;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

/**
 * @author y3tu
 * @date 2018/9/28
 */
public class IPUtilTest {

    @Test
    public void getOutsideIp() {
        Console.log(IPUtil.getOutsideIp());
    }

    @Test
    public void getIpWeatherInfo() {
        Console.log(IPUtil.getIpWeatherInfo("183.232.231.173"));
    }

    @Test
    public void getIpCity() {
        Console.log(IPUtil.getCityInfo("183.232.231.173"));
    }
}