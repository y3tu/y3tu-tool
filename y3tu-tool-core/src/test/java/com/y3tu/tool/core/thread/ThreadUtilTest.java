package com.y3tu.tool.core.thread;

import com.y3tu.tool.core.date.DateUtil;
import com.y3tu.tool.core.date.TimeInterval;
import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


/**
 * 多线程测试
 *
 * @author y3tu
 * @date 2019-03-27
 */
public class ThreadUtilTest {

    @Test
    public void execute() {
        final boolean isValid = false;

        TimeInterval timeInterval = DateUtil.timer();

        ThreadUtil.execute(new Runnable() {
            @Override
            public void run() {
                Console.log(timeInterval.intervalMs());
            }
        });

        Console.log(timeInterval.intervalMs());


    }

    /**
     * 与 Runnable 相比，Callable 可以有返回值，返回值通过 FutureTask 进行封装。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void callableTest() throws ExecutionException, InterruptedException {
        MyCallable myCallable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(myCallable);
        Thread thread = new Thread(futureTask);
        thread.start();
        Console.log(futureTask.get());
    }

    class MyCallable implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            return 123;
        }
    }
}