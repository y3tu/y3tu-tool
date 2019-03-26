package com.y3tu.tool.cron;

import org.junit.Test;

/**
 * 定时任务测试类
 *
 * @author y3tu
 * @date 2019-03-26
 */
public class JobMainTest {

    @Test
    public static void main(String[] args) {
        CronUtil.setMatchSecond(true);
        CronUtil.start(false);
    }

}
