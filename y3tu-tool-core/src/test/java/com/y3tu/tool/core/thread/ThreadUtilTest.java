package com.y3tu.tool.core.thread;

import com.y3tu.tool.core.date.DateUtil;
import com.y3tu.tool.core.date.TimeInterval;
import com.y3tu.tool.core.lang.Console;
import org.junit.Test;


/**
 * @author y3tu
 * @date 2019-03-27
 */
public class ThreadUtilTest {

    @Test
    public void execute() {
        final boolean isValid =  false;

        TimeInterval timeInterval = DateUtil.timer();

        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                Console.log(timeInterval.intervalMs());
            }
        });

        Console.log(timeInterval.intervalMs());


    }
}