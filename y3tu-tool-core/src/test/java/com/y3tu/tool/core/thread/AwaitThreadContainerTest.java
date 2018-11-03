package com.y3tu.tool.core.thread;

import com.y3tu.tool.core.lang.Console;
import org.junit.Test;

import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/11/3
 */
public class AwaitThreadContainerTest {

    @Test
    public void await() throws InterruptedException {
        AwaitThreadContainer awaitThreadContainer = new AwaitThreadContainer();
        Timer timer = new Timer();
        //10秒后唤醒线程
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                awaitThreadContainer.signalAll("test");
            }
        },10000 );

        awaitThreadContainer.await("test", 1000000);

        Console.log("执行结束");
    }

    @Test
    public void signalAll() throws InterruptedException {
        AwaitThreadContainer awaitThreadContainer = new AwaitThreadContainer();
        awaitThreadContainer.signalAll("test");
    }
}