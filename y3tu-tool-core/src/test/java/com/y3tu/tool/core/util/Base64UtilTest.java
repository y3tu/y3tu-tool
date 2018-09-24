package com.y3tu.tool.core.util;

import com.y3tu.tool.core.codec.Base64Util;
import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

/**
 * @author y3tu
 * @date 2018/9/24
 */
public class Base64UtilTest {

    @Test
    public void encodeStr() {

        String test = "www.y3tu.com";
        Console.log(Base64Util.encodeStr(test));
    }
}