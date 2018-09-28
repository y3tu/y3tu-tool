package com.y3tu.tool.core.lang;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/9/28
 */
public class NetUtilTest {

    @Test
    public void getLocalhost() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();;
        Console.log(inetAddress.toString());
    }
    @Test
    public void getIp(){
        Console.log(NetUtil.getInternetIp());
    }
}