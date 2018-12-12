package com.y3tu.tool.core.util;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018-12-11
 */
public class IdUtilTest {

    @Test
    public void createSnowflake() {
        Long id = IdUtil.createSnowflake(1,1).nextId();
        Console.log(id);
    }
}