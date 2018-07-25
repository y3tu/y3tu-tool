package com.y3tu.tool.core.io;

import com.y3tu.tool.core.lang.Assert;
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
}