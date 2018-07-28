package com.y3tu.tool.core.io;

import com.y3tu.tool.core.lang.Assert;
import com.y3tu.tool.core.lang.Console;
import org.junit.Test;


/**
 * @author y3tu
 * @date 2018/7/25
 */
public class FileUtilTest {

    @Test
    public void isWindows() {
        Assert.isTrue(FileUtil.isWindows());
    }

    @Test
    public void ls() {
        Console.log(FileUtil.ls("classpath:com/y3tu/tool/core/util")[0].getAbsolutePath());
    }
}