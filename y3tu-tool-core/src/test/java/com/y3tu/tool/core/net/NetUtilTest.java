package com.y3tu.tool.core.net;

import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.core.lang.NetUtil;
import com.y3tu.tool.core.text.ObjectPrintUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;


/**
 * NetUtil单元测试
 * @author y3tu
 */
public class NetUtilTest {

    @Test
    public void test() {
        Console.log(ObjectPrintUtil.toPrettyString(NetUtil.getLocalMacAddress()));
    }

    /**
     * 是否是可用端口
     */
    @Test
    public void isUsableLocalPort() {
        Assert.assertTrue(NetUtil.isUsableLocalPort(1));
    }

    /**
     * 根据ip地址计算出long型的数据
     */
    @Test
    public void ipv4ToLong() {
        Console.log(NetUtil.ipv4ToLong("104.224.153.202"));
    }


    @Test
    public void toAbsoluteUrl() {
        Console.log(NetUtil.toAbsoluteUrl("file:/Users/yxy/work/","dfd/work"));
    }

    @Test
    public void hideIpPart() {
        Console.log(NetUtil.hideIpPart("104.224.153.202"));
    }

    @Test
    public void buildInetSocketAddress() {
        InetSocketAddress inetSocketAddress = NetUtil.buildInetSocketAddress("104.224.153.202:27506",8080);
        Console.log(inetSocketAddress.getAddress());
    }

    @Test
    public void getIpByHost(){
        Console.log(NetUtil.getIpByHost("www.y3tu.com"));
    }


    @Test
    public void createAddress() {
        InetSocketAddress inetSocketAddress=  NetUtil.createAddress("104.224.153.202",27506);
        Console.log(inetSocketAddress.getHostString());
    }

    @Test
    public void netCat() throws IOException {
        byte[] bytes = new byte[]{1,2};
        NetUtil.netCat("104.224.153.202",27506,bytes);
    }
}