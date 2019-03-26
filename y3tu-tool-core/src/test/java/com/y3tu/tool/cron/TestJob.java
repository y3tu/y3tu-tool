package com.y3tu.tool.cron;

import com.y3tu.tool.core.date.DateUtil;
import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.core.thread.ThreadUtil;
import com.y3tu.tool.core.util.IdUtil;

/**
 * 测试定时任务，当触发到定时的时间点时，执行doTest方法
 *
 * @author looly
 */
public class TestJob {

    private String jobId = IdUtil.simpleUUID();

    /**
     * 执行定时任务内容
     */
    public void doTest() {
//		String name = Thread.currentThread().getName();
        Console.log("Test Job {} running... at {}", jobId, DateUtil.now());
    }

    /**
     * 执行循环定时任务，测试在定时任务结束时作为deamon线程是否能正常结束
     */
    public void doWhileTest() {
        String name = Thread.currentThread().getName();
        while (true) {
            Console.log("Job {} while running...", name);
            ThreadUtil.sleep(2000);
        }
    }
}
