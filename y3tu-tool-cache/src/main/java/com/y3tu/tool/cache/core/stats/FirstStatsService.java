package com.y3tu.tool.cache.core.stats;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.caffeine.CaffeineCache;
import com.y3tu.tool.cache.core.manager.FirstCacheManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 统计服务
 *
 * @author y3tu
 */
public class FirstStatsService extends StatsService {

    /**
     * 缓存管理
     */
    private FirstCacheManager cacheManager;

    @Override
    public List<CacheStatsInfo> listCacheStats(String cacheName) {
        List<CacheStatsInfo> cacheStatsInfoList = new ArrayList<>();

        Collection<Cache> cacheCollection = cacheManager.getCache(cacheName);
        for (Cache cache : cacheCollection) {
            CaffeineCache caffeineCache = (CaffeineCache) cache;
            CacheStatsInfo cacheStatsInfo = new CacheStatsInfo();
            cacheStatsInfo.setCacheName(cacheName);
            cacheStatsInfo.setInternalKey(caffeineCache.getLayeringCacheSetting().getInternalKey());
            cacheStatsInfo.setDepict(caffeineCache.getLayeringCacheSetting().getDepict());
            cacheStatsInfo.setLayeringCacheSetting(caffeineCache.getLayeringCacheSetting());

            CacheStats firstCacheStats = caffeineCache.getCacheStats();
            // 清空加载缓存时间
            firstCacheStats.getAndResetCachedMethodRequestTime();

            cacheStatsInfo.setRequestCount(cacheStatsInfo.getRequestCount() + firstCacheStats.getAndResetCacheRequestCount());
            cacheStatsInfo.setMissCount(cacheStatsInfo.getMissCount() + firstCacheStats.getAndResetCachedMethodRequestCount());
            cacheStatsInfo.setTotalLoadTime(cacheStatsInfo.getTotalLoadTime() + firstCacheStats.getAndResetCachedMethodRequestTime());
            cacheStatsInfo.setHitRate((cacheStatsInfo.getRequestCount() - cacheStatsInfo.getMissCount()) / (double) cacheStatsInfo.getRequestCount() * 100);

            cacheStatsInfo.setFirstCacheRequestCount(cacheStatsInfo.getFirstCacheRequestCount() + firstCacheStats.getAndResetCacheRequestCount());
            cacheStatsInfo.setFirstCacheMissCount(cacheStatsInfo.getFirstCacheMissCount() + firstCacheStats.getAndResetCachedMethodRequestCount());

            cacheStatsInfoList.add(cacheStatsInfo);
        }

        return cacheStatsInfoList;
    }

    @Override
    public void resetCacheStat() {

    }


}
