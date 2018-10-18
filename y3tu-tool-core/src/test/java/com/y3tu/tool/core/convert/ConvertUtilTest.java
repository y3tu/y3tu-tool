package com.y3tu.tool.core.convert;

import com.y3tu.tool.core.convert.impl.CharsetConverter;
import com.y3tu.tool.core.lang.Console;
import org.junit.Assert;
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
        ConvertUtil.register(new CharsetConverter(), Charset.class);
        Object result = ConvertUtil.convert("UTF-8", Charset.class);
        ConvertUtil.deregister();
        Console.log(result.getClass().toString());
    }

    @Test
    public void convertCommon() {
        Object value = ConvertUtil.convert("111", Integer.class);
        Assert.assertTrue(value instanceof Integer);
    }

    @Test
    public void primitiveToWrapperTest(){
        Class clazz = ConvertUtil.primitiveToWrapper(int.class);
        Assert.assertEquals("java.lang.Integer",clazz.getTypeName());
    }

    @Test
    public void toLong() {
        long result = ConvertUtil.toLong("dd",0l);
        Assert.assertEquals(0,result);
    }

    @Test
    public void toStrArray() {
        int[] a = {1,2,3};
        String[] obj = (String[])ConvertUtil.convert(a,String[].class);

        Console.log(obj.toString());

    }
}