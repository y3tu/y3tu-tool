package com.y3tu.tool.core.swing;

import com.y3tu.tool.core.io.FileUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/10/15
 */
public class RobotUtilTest {

    @Test
    public void captureScreenTest() {
        RobotUtil.captureScreen(FileUtil.file("/Users/yxy/work/screen.jpg"));
    }
}