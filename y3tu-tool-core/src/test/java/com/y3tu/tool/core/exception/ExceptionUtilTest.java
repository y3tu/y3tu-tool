package com.y3tu.tool.core.exception;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExceptionUtilTest {

    @Test
    public void getRootStackElement() {
        String msg = ExceptionUtil.getRootStackElement().toString();
        Console.error(msg);
    }
}