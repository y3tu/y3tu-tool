package com.y3tu.tool.core.util;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import static org.junit.Assert.*;

public class PageUtilTest {

    @Test
    public void rainbow() {
        int[] a = PageUtil.rainbow(1, 10);
        Console.log(ObjectUtil.toPrettyString(a));
    }
}