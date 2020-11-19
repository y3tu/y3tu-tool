package com.y3tu.tool.core.thread;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import org.junit.Ignore;
import org.junit.Test;


/**
 * 高并发测试
 *
 * @author y3tu
 */
public class ConcurrencyTesterTest {

    @Test
    @Ignore
    public void concurrencyTesterTest() {
        ConcurrencyTester tester = ThreadUtil.concurrencyTest(1000, () -> {
            long delay = RandomUtil.randomLong(100, 1000);
            ThreadUtil.sleep(delay);
            Console.log("{} test finished, delay: {}", Thread.currentThread().getName(), delay);
        });
        Console.log(tester.getInterval());
    }
}