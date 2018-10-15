package com.y3tu.tool.core.codec;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/10/15
 */
public class DigestUtilTest {

    @Test
    public void md5Hex() {
        String resutStr = DigestUtil.md5Hex("y3tu");
        Console.log(resutStr);
    }
}