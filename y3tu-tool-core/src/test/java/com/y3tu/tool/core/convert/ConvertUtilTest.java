package com.y3tu.tool.core.convert;

import com.y3tu.tool.core.convert.impl.CharsetConverter;
import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/8/1
 */
public class ConvertUtilTest {

    @Test
    public void convert() {
        ConvertUtil.register(new CharsetConverter(),Charset.class);
        Object result = ConvertUtil.convert("UTF-8", Charset.class);
        ConvertUtil.deregister();
        Console.log(result.getClass().toString());

    }
}