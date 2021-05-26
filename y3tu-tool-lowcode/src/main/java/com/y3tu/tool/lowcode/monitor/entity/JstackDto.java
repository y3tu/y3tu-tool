package com.y3tu.tool.lowcode.monitor.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 线程信息总览
 *
 * @author y3tu
 */
@Data
@AllArgsConstructor
public class JstackDto {
    /**
     * 进程id
     */
    private String pid;
    /**
     * 总线程数
     */
    private int threadCountTotal;
    /**
     * 运行线程数
     */
    private int runnableThreadCount;
    /**
     * 等待唤醒的线程数，有时限，一般出现在调用wait(long), join(long)等情况下, 另外一个线程sleep后休眠的线程数
     */
    private int timedWaitingThreadCount;

    /**
     * 无限等待唤醒的线程数，线程拥有了某个锁之后, 调用了他的wait方法, 等待的线程数
     */
    private int waitingThreadCount;
}
