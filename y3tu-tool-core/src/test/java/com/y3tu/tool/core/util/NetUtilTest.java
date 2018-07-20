package com.y3tu.tool.core.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class NetUtilTest {

    @Test
    public void longToIpv4() {
        String ipv4 = NetUtil.longToIpv4(2130706433L);
        Assert.assertEquals("127.0.0.1", ipv4);
    }
}