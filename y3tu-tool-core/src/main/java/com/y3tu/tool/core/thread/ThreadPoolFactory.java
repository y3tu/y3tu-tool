package com.y3tu.tool.core.thread;

import com.y3tu.tool.core.exception.ThreadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.concurrent.*;

/**
 * 线程池工厂
 *
 * @author y3tu
 * @date 2018/6/16
 */
@Slf4j
public class ThreadPoolFactory {
    /**
     * 线程池map
     */
    private static ConcurrentHashMap<String, GeneralThreadPool> tpHash = new ConcurrentHashMap<String, GeneralThreadPool>();

    /**
     * 默认核心线程存活时间
     */
    private static final long DEFAULTKEEPALIVETIME = 60;
    /**
     * 默认核心线程存活时间
     */
    private static final TimeUnit DEFAULTUNIT = TimeUnit.SECONDS;
    private static final int DEFAULT_QUEUE_SIZE = 20;
    /**
     * 默认拒绝策略
     */
    private static final RejectedExecutionHandler DEFAULTHANDLER = new ThreadPoolExecutor.CallerRunsPolicy();

    /**
     * 默认线程工厂
     *
     * @param name
     * @return
     * @throws ThreadException
     */
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = Executors.defaultThreadFactory();

    /**
     * 创建单个线程concurrent线程池
     *
     * @param name 线程池名
     * @return GeneralThreadPool 线程池
     * @throws ThreadException ThreadException
     */
    public static GeneralThreadPool createSingleThreadPool(String name) throws ThreadException {
        return createGeneralThreadPool(name, 1, 1, DEFAULTKEEPALIVETIME, DEFAULTUNIT, DEFAULT_QUEUE_SIZE,
                new LinkedBlockingQueue<Runnable>(), DEFAULT_THREAD_FACTORY, DEFAULTHANDLER);
    }

    /**
     * 创建固定大小concurrent线程池(不使用队列,直接提交策略)
     *
     * @param name         线程池名
     * @param corePoolSize 核心池大小
     * @return GeneralThreadPool 线程池
     * @throws ThreadException 异常
     */
    public static GeneralThreadPool createGeneralThreadPool(String name,
                                                            int corePoolSize) throws ThreadException {
        return createGeneralThreadPool(name, corePoolSize, corePoolSize, DEFAULTKEEPALIVETIME, DEFAULTUNIT, DEFAULT_QUEUE_SIZE,
                new SynchronousQueue<Runnable>(), DEFAULT_THREAD_FACTORY, DEFAULTHANDLER);
    }

    /**
     * 创建固定大小concurrent线程池(使用有界队列)
     *
     * @param name         线程池名
     * @param corePoolSize 核心池大小
     * @param queueSize    队列大小
     * @return GeneralThreadPool 线程池
     * @throws ThreadException 异常
     */
    public static GeneralThreadPool createGeneralBoundedThreadPool(String name, int corePoolSize, int queueSize) throws ThreadException {
        return createGeneralThreadPool(name, corePoolSize, corePoolSize, DEFAULTKEEPALIVETIME, DEFAULTUNIT, DEFAULT_QUEUE_SIZE,
                new ArrayBlockingQueue<Runnable>(queueSize), DEFAULT_THREAD_FACTORY, DEFAULTHANDLER);
    }

    /**
     * 创建固定大小concurrent线程池(使用无界队列)
     *
     * @param name         线程池名
     * @param corePoolSize 核心池大小
     * @return GeneralThreadPool 线程池
     * @throws ThreadException 异常
     */
    public static GeneralThreadPool createGeneralUnboundedThreadPool(String name, int corePoolSize) throws ThreadException {
        return createGeneralThreadPool(name, corePoolSize, corePoolSize, DEFAULTKEEPALIVETIME, DEFAULTUNIT, DEFAULT_QUEUE_SIZE,
                new LinkedBlockingQueue<Runnable>(), DEFAULT_THREAD_FACTORY, DEFAULTHANDLER);
    }

    /**
     * 创建concurrent线程池
     *
     * @param name            线程池名
     * @param corePoolSize    核心池大小
     * @param maximumPoolSize 线程池最大线程数
     * @param keepAliveTime   核心线程存活时间
     * @param unit            核心线程存活时间单位
     * @param queue           队列
     * @param handler         拒绝策略
     * @return GeneralThreadPool 线程池
     * @throws ThreadException 异常
     */
    public static GeneralThreadPool createGeneralThreadPool(String name,
                                                            int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                                            TimeUnit unit, int queueSize, BlockingQueue<Runnable> queue, ThreadFactory threadFactory,
                                                            RejectedExecutionHandler handler) throws ThreadException {
        checkThreadPoolName(name);
        ThreadPoolExecutor executor = null;

        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, queue, threadFactory, handler);

        GeneralThreadPool cte = new GeneralThreadPool(name, corePoolSize, queueSize, executor);
        log.info("Create concurrent thread pool,the name:{},corePoolSize:{},maxPoolSize:{}",
                name, String.valueOf(corePoolSize),
                String.valueOf(maximumPoolSize));
        GeneralThreadPool pool = tpHash.putIfAbsent(name, cte);
        return cte;

    }

    /**
     * 校验是否线程池已经存在
     *
     * @param name
     * @return boolean
     */
    public static boolean checkThreadPoolExist(String name) {
        return tpHash.containsKey(name);
    }

    /**
     * 校验线程池名
     *
     * @param name
     * @throws ThreadException
     */
    private static void checkThreadPoolName(String name) throws ThreadException {
        Assert.notNull(name, "线程名字不能为空");
        if (tpHash.containsKey(name)) {
            new ThreadException("非法的线程名字");
        }
    }

    /**
     * 删除线程池
     *
     * @param name
     */
    public static void removeThreadPoolByName(String name) {
        tpHash.remove(name);
    }

    /**
     * 根据名字获取线程池
     *
     * @param name 线程池名
     * @return GeneralThreadPool 线程池
     */
    public static GeneralThreadPool getThreadPoolByName(String name) {
        Assert.notNull(name, "线程名字不能为空");
        if (tpHash.containsKey(name)) {
            return tpHash.get(name);
        } else {
            return null;
        }
    }
}
