package com.y3tu.tool.core.collection;

import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.core.lang.Editor;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayUtilTest {

    @Test
    public void newArray() {
        String[] arrs = ArrayUtil.newArray(String.class, 1);
        Console.log(arrs.toString());
    }

    @Test
    public void max() {
        String[] nums = {"1", "2", "3"};
        String str = ArrayUtil.min(nums);
        Console.log(str);
    }

    @Test
    public void swap() {
        String[] nums = {"1", "2", "3"};
        String[] nums1 = ArrayUtil.swap(nums, 0, 2);
        Console.log(nums1.toString());
    }

    @Test
    public void filter() {
        Integer[] a = {1, 2, 3, 4, 5, 6};
        Integer[] filter = ArrayUtil.filter(a, (t) -> (t % 2 == 0) ? true : false);
        Assert.assertArrayEquals(filter, new Integer[]{2, 4, 6});
    }

    @Test
    public void removeNull() {
        Integer[] a = {1, 2, 3, null, 5, 6};
        Integer[] removeNull = ArrayUtil.removeNull(a);
        Assert.assertArrayEquals(removeNull, new Integer[]{1, 2, 3, 5, 6});

    }
}