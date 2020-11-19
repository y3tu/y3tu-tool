package com.y3tu.tool.core.thread;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 线程工具测试
 *
 * @author y3tu
 */
public class ThreadUtilTest {

    @Test
    public void executeTest() throws InterruptedException {
        final boolean isValid = true;
        ThreadUtil.newThread(() -> {
            long delay = RandomUtil.randomLong(1000000, 100000000);
            Console.log("{} test finished, delay: {}", Thread.currentThread().getName(), delay);
        }, "y3tu-test").start();

        ThreadUtil.execute(() -> {
            long delay = RandomUtil.randomLong(1000000, 100000000);
            Console.log("{} test finished, delay: {}", Thread.currentThread().getName(), delay);
        });

        Thread.sleep(20000);
    }

    @Test
    public void threadPoolTest() throws InterruptedException {
        ExecutorService executorService = ThreadUtil.newExecutor(20, 30, 1000);
        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(99);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                long delay = RandomUtil.randomLong(1000, 10000);
                System.out.println("delay:" + delay);
                ThreadUtil.sleep(delay);
                Console.log("{} test finished, delay: {}", Thread.currentThread().getName(), delay);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
    }

    @Test
    public void nameThreadTest() throws InterruptedException {
        ThreadFactory threadFactory = ThreadUtil.newNamedThreadFactory("y3tu-pool",false);
        ExecutorService executorService = ThreadUtil.newExecutor(20, 30, 1000,threadFactory);

        CountDownLatch countDownLatch = ThreadUtil.newCountDownLatch(99);
        for (int i = 0; i < 100; i++) {
            Thread thread = ThreadUtil.newNamedThreadFactory("y3tu-test", true).newThread(() -> {
                long delay = RandomUtil.randomLong(1000, 10000);
                ThreadUtil.sleep(delay);
                Console.log("{} test finished, delay: {}", Thread.currentThread().getName(), delay);
                countDownLatch.countDown();
            });
            executorService.submit(thread);
        }
        countDownLatch.await();
        executorService.shutdown();
    }

}