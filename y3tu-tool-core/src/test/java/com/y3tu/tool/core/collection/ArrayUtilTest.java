package com.y3tu.tool.core.collection;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayUtilTest {

    @Test
    public void newArray() {
        String[] arrs = ArrayUtil.newArray(String.class,1);
        Console.log(arrs.toString());
    }

    @Test
    public void max() {
        String[] nums = {"1","2","3"};
        String str = ArrayUtil.min(nums);
        Console.log(str);
    }

    @Test
    public void swap() {
        String[] nums = {"1","2","3"};
        String[] nums1 =  ArrayUtil.swap(nums,0,2);
        Console.log(nums1.toString());
    }
}