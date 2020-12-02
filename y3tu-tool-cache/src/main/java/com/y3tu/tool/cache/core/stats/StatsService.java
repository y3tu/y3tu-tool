package com.y3tu.tool.cache.core.stats;

import java.util.List;

/**
 * 统计服务
 *
 * @author y3tu
 */
public abstract class StatsService {

    /**
     * 统计特定缓存名的缓存信息
     *
     * @param cacheName 缓存名
     * @return
     */
    abstract List<CacheStatsInfo> listCacheStats(String cacheName);
    
    /**
     * 关闭统计服务时调用
     */
    void destroy() {
    }

    /**
     * 重置缓存统计
     */
    abstract void resetCacheStat();
}
