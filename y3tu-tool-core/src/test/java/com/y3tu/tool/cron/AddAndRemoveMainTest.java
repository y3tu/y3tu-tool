package com.y3tu.tool.cron;


import com.y3tu.tool.core.lang.Console;
import com.y3tu.tool.core.thread.ThreadUtil;

public class AddAndRemoveMainTest {

    public static void main(String[] args) {
        CronUtil.setMatchSecond(true);
        CronUtil.start(true);
        CronUtil.getScheduler().clear();
        String id = CronUtil.schedule("*/2 * * * * *", new Runnable() {
            @Override
            public void run() {
                Console.log("task running : 2s");
            }
        });
        ThreadUtil.sleep(3000);
        CronUtil.remove(id);
        Console.log("Task Removed");
        id = CronUtil.schedule("*/3 * * * * *", new Runnable() {

            @Override
            public void run() {
                Console.log("New task add running : 3s");
            }
        });
        Console.log("New Task added.");
    }
}
