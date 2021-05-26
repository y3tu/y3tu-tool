package com.y3tu.tool.lowcode.monitor.jvm;

import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.core.util.SystemUtil;
import com.y3tu.tool.lowcode.monitor.entity.JstackDto;

/**
 * 查看线程信息
 *
 * @author y3tu
 */
public class Jstack {

    private final static String PREFIX = "java.lang.Thread.State: ";

    /**
     * 获取线程总览信息
     *
     */
    public static JstackDto jstack() {
        //当前应用进程id
        String currentPid = String.valueOf(SystemUtil.getCurrentPID());
        //执行jstack命令
        String cmdResult = SystemUtil.executeCmd(new String[]{"jstack", currentPid});
        int threadCountTotal = StrUtil.appearNumber(cmdResult, "nid=");
        int runnableThreadCount = StrUtil.appearNumber(cmdResult, PREFIX + "RUNNABLE");
        int timedWaitingThreadCount = StrUtil.appearNumber(cmdResult, PREFIX + "TIMED_WAITING");
        int waitingThreadCount = StrUtil.appearNumber(cmdResult, PREFIX + "WAITING");
        return new JstackDto(currentPid,threadCountTotal,runnableThreadCount,timedWaitingThreadCount,waitingThreadCount);
    }

}
