package com.y3tu.tool.core.text;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/10/5
 */
public class StringUtilsTest {

    /**
     * 将下划线方式命名的字符串转换为驼峰式
     */
    @Test
    public void toCamelCase() {
        Assert.assertEquals("sysUser", StringUtils.toCamelCase("sys-user" ));
        Assert.assertEquals("sysUser", StringUtils.toCamelCase("sys-user" ));

    }
}