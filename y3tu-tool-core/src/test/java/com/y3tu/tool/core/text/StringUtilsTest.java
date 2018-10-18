package com.y3tu.tool.core.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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

    @Test
    public void splitToList() {

        List<String> list = StringUtils.splitToList("ddddx,ddsd,dd,22,d",CharUtil.COLON);
        Assert.assertEquals(5,list.size());

        List<String> list1 = StringUtils.splitToList("ddddx,ddsd,dd,22,d",",",3);
        Assert.assertEquals(3,list1.size());
    }
}