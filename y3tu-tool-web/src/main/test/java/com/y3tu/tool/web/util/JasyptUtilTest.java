package com.y3tu.tool.web.util;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/8/31
 */
public class JasyptUtilTest {

    @Test
    public void encyptPwd() {
        Console.log(JasyptUtil.encyptPwd("123456","yxy727"));
    }
}