package com.y3tu.tool.cache.core.support;

import com.y3tu.tool.core.thread.RejectPolicy;
import com.y3tu.tool.core.thread.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 缓存默认创建一个常住线程池
 *
 * @author yuhao.wang3
 */
public class ThreadTaskUtils {
    private static ExecutorService executor = null;

    static {
        ThreadFactory threadFactory = ThreadUtil.newNamedThreadFactory("y3tu-tool-cache", true);
        executor = ThreadUtil.newExecutor(8, 64, 1000, 120, threadFactory, RejectPolicy.DISCARD);
    }

    public static void run(Runnable runnable) {
        executor.execute(runnable);
    }
}
