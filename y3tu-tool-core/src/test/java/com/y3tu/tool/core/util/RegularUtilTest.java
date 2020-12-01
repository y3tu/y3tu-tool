package com.y3tu.tool.core.util;

import org.junit.Assert;
import org.junit.Test;


/**
 * 正则测试类
 *
 * @author y3tu
 */
public class RegularUtilTest {

    @Test
    public void contains() {
        Assert.assertTrue(RegularUtil.contains(RegularUtil.EMAIL, "14643092@qq.com"));
    }
}