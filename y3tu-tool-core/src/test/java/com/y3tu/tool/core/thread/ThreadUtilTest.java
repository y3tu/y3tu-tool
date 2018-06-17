package com.y3tu.tool.core.thread;

import com.y3tu.tool.core.exception.ThreadException;
import org.junit.Test;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.Assert.*;

/**
 * @author y3tu
 * @date 2018/6/16
 */
public class ThreadUtilTest {

    @Test
    public void excAsync() throws InterruptedException {
        try {
            GeneralThreadPool generalThreadPool = ThreadUtil.createThreadPool();

            for (int i = 0; i < 100; i++) {

                Runnable runnable = new Runnable() {
                    @Override
                    public void run()
                    {
                        System.out.println(Thread.currentThread().getName());
                    }

                };

                generalThreadPool.executeTask(runnable);

            }


            while(generalThreadPool.getActiveCount()>0){

            }
            generalThreadPool.getThreadPoolInfo();
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx");

        } catch (ThreadException e) {
            e.printStackTrace();
        }




    }
}