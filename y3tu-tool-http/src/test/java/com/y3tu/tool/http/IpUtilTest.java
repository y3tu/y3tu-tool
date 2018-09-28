package com.y3tu.tool.http;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/9/28
 */
public class IpUtilTest {

    @Test
    public void getOutsideIp() {
        Console.log(IpUtil.getOutsideIp());
    }
}