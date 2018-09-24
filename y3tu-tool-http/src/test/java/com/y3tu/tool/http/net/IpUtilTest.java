package com.y3tu.tool.http.net;

import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.http.IpUtil;
import org.junit.Test;

public class IpUtilTest {

    @Test
    public void getIpWeatherInfo() {
        Console.log(IpUtil.getIpWeatherInfo("183.232.231.173"));
    }

    @Test
    public void getIpCity() {
        Console.log(IpUtil.getIpCity("183.232.231.173"));
    }
}