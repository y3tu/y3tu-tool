package com.y3tu.tool.core.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池类
 *
 * @author y3tu
 * @date 2018/6/16
 */
@Slf4j
public class GeneralThreadPool {

    public static final int CONCURRENT_THREAD_POOL = 0;

    /**
     * 线程池
     */
    private ThreadPoolExecutor threadPool = null;
    /**
     * 线程池类型
     */
    private int poolType = ThreadPoolType.CONCURRENT_THREAD_POOL;
    /**
     * 线程池名
     */
    private String name = null;
    private int corePoolSize;
    private int queueSize;
    private long sleepTime = 10000;


    /**
     * 构造函数
     *
     * @param name       线程池名
     * @param threadPool 线程池
     */
    public GeneralThreadPool(String name, int corePoolSize, int queueSize, ThreadPoolExecutor threadPool) {
        this.name = name;
        this.corePoolSize = corePoolSize;
        this.queueSize = queueSize;
        this.threadPool = threadPool;
        threadPool.isTerminated();
    }

    /**
     * 读取名字
     *
     * @return 名字
     */
    public String getName() {
        return name;
    }

    /**
     * 打印线程池使用信息
     */
    public String getThreadPoolInfo() {
        StringBuffer sb = new StringBuffer();

        sb.append("\n" + this.getName() + "线程池使用信息：			");
        sb.append("\n===========================			");
        sb.append("\n										");
        sb.append("\n活跃线程数量		:" + this.getActiveCount());
        sb.append("\n阻塞队列大小		:" + this.getBlockQueueSize());
        sb.append("\n已完成执行任务总数	:" + this.getCompletedTaskCount());
        sb.append("\n计划执行任务总数	:" + this.getTaskCount());
        sb.append("\n										");
        sb.append("\n===========================			");
        log.info(sb.toString());
        return sb.toString();
    }

    /**
     * 执行线程
     *
     * @param command 线程
     */
    public void execute(Runnable command) {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            threadPool.execute(command);
        }
    }

    /**
     * 不使用拒绝策略
     * @param engine
     */
    public void executeTask(Runnable engine) {

        while (this.getActiveCount() == corePoolSize && this.getBlockQueueSize() == queueSize) {
            try {
                log.info("任务队列满载，休眠" + sleepTime + "ms！等待任务执行完成。。。。");
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

        if (engine != null) {
            this.execute(engine);
        } else {
            log.error("任务为空，不能加入线程池执行！！！");
        }

    }

    /**
     * 提交线程
     *
     * @param command 线程
     * @return 期望
     */
    public Future<?> submit(Runnable command) {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            return threadPool.submit(command);
        } else {
            return null;
        }
    }

    /**
     * 提交线程
     *
     * @param <T>
     * @param task   线程
     * @param result 结果
     * @return 期望
     */

    public <T> Future<T> submit(Runnable task, T result) {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            return threadPool.submit(task, result);
        } else {
            return null;
        }
    }

    /**
     * 提交线程
     *
     * @param <T>
     * @param task 线程
     * @return 期望
     */
    public <T> Future<T> submit(Callable<T> task) {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            return threadPool.submit(task);
        } else {
            return null;
        }
    }


    /**
     * 设置核心线程数
     * 此操作将重写构造方法中设置的任何值。 如果新值小于当前值，则多余的现有线程将在下一次空闲时终止。
     * 如果较大，则在需要时启动新线程来执行这些排队的任务
     *
     * @param corePoolSize 核心线程数
     */
    public void setCorePoolSize(int corePoolSize) {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            this.threadPool.setCorePoolSize(corePoolSize);
        }
    }

    /**
     * 设置线程在终止前可以保持空闲的时间限制
     * 如果池中的当前线程数多于核心线程数，在不处理任务的情况下等待这一时间段之后，
     * 多余的线程将被终止。此操作将重写构造方法中设置的任何值。
     *
     * @param time 时间
     */
    public void setKeepAliveTime(long time) {
        //默认为秒
        this.setKeepAliveTime(time, TimeUnit.SECONDS);
    }

    /**
     * 设置线程在终止前可以保持空闲的时间限制
     * 如果池中的当前线程数多于核心线程数，在不处理任务的情况下等待这一时间段之后，
     * 多余的线程将被终止。此操作将重写构造方法中设置的任何值
     *
     * @param time 时间
     * @param unit 时间单位
     */
    public void setKeepAliveTime(long time, TimeUnit unit) {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            this.threadPool.setKeepAliveTime(time, unit);
        }
    }

    /**
     * 设置允许的最大线程数
     * 此操作将重写构造方法中设置的任何值。 如果新值小于当前值，则多余的现有线程将在下一次空闲时终止。
     *
     * @param maximumPoolSize 最大线程数
     */
    public void setMaximumPoolSize(int maximumPoolSize) {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            this.threadPool.setMaximumPoolSize(maximumPoolSize);
        }
    }

    /**
     * 获取阻塞队列大小
     *
     * @return 队列大小
     */
    public int getBlockQueueSize() {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            return this.threadPool.getQueue().size();
        } else {
            return 0;
        }

    }

    /**
     * 获取活跃线程数量
     *
     * @return 活跃线程数量
     */
    public int getActiveCount() {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            return this.threadPool.getActiveCount();
        } else {
            return 0;
        }

    }

    /**
     * 返回已完成执行的任务总数
     *
     * @return 任务总数
     */
    public long getCompletedTaskCount() {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            return this.threadPool.getCompletedTaskCount();
        } else {
            return 0;
        }

    }

    /**
     * 返回计划执行的任务总数
     *
     * @return 任务总数
     */
    public long getTaskCount() {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            return this.threadPool.getTaskCount();
        } else {
            return 0;
        }

    }

    /**
     * 是否还有执行队列
     *
     * @return 是否还有执行队列
     */
    public boolean hasMoreAcquire() {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            return !this.threadPool.getQueue().isEmpty();
        } else {
            return false;
        }

    }


    /**
     * 关闭线程池
     * 线程池处于SHUTDOWN状态，此时线程池不能够接受新的任务，它会等待所有任务执行完毕
     */
    public void shutdown() {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            this.threadPool.shutdown();
            ThreadPoolFactory.removeThreadPoolByName(name);
            this.threadPool = null;
            log.info("The thread pool{} is shutdown.", name);
        }
    }

    /**
     * 关闭线程池
     * 线程池处于STOP状态，此时线程池不能接受新的任务，并且会去尝试终止正在执行的任务
     */
    public void shutdownNow() {
        if (poolType == ThreadPoolType.CONCURRENT_THREAD_POOL) {
            this.threadPool.shutdownNow();
            ThreadPoolFactory.removeThreadPoolByName(name);
            this.threadPool = null;
            log.info("The thread pool[{0}] is shutdown.", name);
        }
    }

    class ThreadPoolType {

        /**
         * CONCURRENT_THREAD_POOL
         */
        public static final int CONCURRENT_THREAD_POOL = 0;

    }

}
