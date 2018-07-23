package com.y3tu.tool.core.lang;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/7/23
 */
public class ValidatorTest {

    @Test
    public void isNumber() {
        Assert.assertTrue(Validator.isNumber("1111a"));
    }
}