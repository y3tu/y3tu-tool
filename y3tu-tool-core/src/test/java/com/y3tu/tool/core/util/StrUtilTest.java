package com.y3tu.tool.core.util;

import org.junit.Assert;
import org.junit.Test;


/**
 * @author y3tu
 * @date 2018-12-07
 */
public class StrUtilTest {

    @Test
    public void hide() {
        String name = "谭美丽";
        String hideName = StrUtil.hide(name, 1, 4);
        Assert.assertEquals("谭**", hideName);
    }
}