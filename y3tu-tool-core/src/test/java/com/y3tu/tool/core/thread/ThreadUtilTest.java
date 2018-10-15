package com.y3tu.tool.core.thread;

import org.junit.Assert;
import org.junit.Test;



/**
 * @author y3tu
 * @date 2018/6/16
 */
public class ThreadUtilTest {

    @Test
    public void executeTest() {
        final boolean isValid = true;

        ThreadUtil.execute(new Runnable() {

            @Override
            public void run() {
                Assert.assertTrue(isValid);
            }
        });

    }
}