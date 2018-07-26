package com.y3tu.tool.core.io;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import static org.junit.Assert.*;

public class FilePathUtilTest {

    @Test
    public void normalizePath() {
        Console.log(FilePathUtil.normalizePath("/Users/yxy/work"));
    }
}