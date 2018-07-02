package com.y3tu.tool.core.base;

import org.junit.Test;

import static org.junit.Assert.*;

public class RuntimeUtilTest {

    @Test
    public void test() {
        int pid = RuntimeUtil.getPid();
        System.out.println(pid);
        long upTime = RuntimeUtil.getUpTime();
        System.out.println(upTime);
        String vmArguments = RuntimeUtil.getVmArguments();
        System.out.println(vmArguments);
        int cores = RuntimeUtil.getCores();
        System.out.println(cores);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("执行结束的钩子的函数");
            }
        };
        RuntimeUtil.addShutdownHook(runnable);
    }
}