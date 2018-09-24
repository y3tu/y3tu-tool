package com.y3tu.tool.core.math;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/9/24
 */
public class NumberUtilTest {

    @Test
    public void min() {

        int a = 1;
        int b = 2;
        int c = 3;
        int result = NumberUtil.min(1, 2, 3);
        Assert.assertEquals(1, result);
    }
}