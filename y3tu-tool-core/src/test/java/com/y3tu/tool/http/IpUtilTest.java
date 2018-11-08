package com.y3tu.tool.http;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

/**
 * @author y3tu
 * @date 2018/11/8
 */
public class IpUtilTest {

    final String IP = "132.232.11.210";

    @Test
    public void getIpAddr() {
    }

    @Test
    public void getIpWeatherInfo() {
        String weather = IpUtil.getIpWeatherInfo(IP);
        Console.log(weather);
    }

    /**
     * 根据ip获取城市信息
     */
    @Test
    public void getCityInfo() {
        String city = IpUtil.getCityInfo(IP);
        Console.log(city);
    }

    @Test
    public void getOutsideIp() {
    }
}