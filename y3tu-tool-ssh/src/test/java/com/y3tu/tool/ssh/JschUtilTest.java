package com.y3tu.tool.ssh;

import com.jcraft.jsch.Session;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/7/26
 */
public class JschUtilTest {

    @Test
    public void bindPort() {
        Session session = JschUtil.getSession("136.6.80.95",22,"websph","abc123");
        JschUtil.bindPort(session,"136.6.128.221",8081,8082);

    }
}