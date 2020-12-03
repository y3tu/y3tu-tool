package com.y3tu.tool.cache.core.manager;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.caffeine.CaffeineCache;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import com.y3tu.tool.cache.core.stats.CacheStats;
import com.y3tu.tool.cache.core.stats.CacheStatsInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 一级缓存管理
 *
 * @author y3tu
 */
@Slf4j
public class FirstCacheManager extends AbstractCacheManager {

    public FirstCacheManager(boolean stats) {
        this.stats = stats;
    }


    @Override
    protected Cache getMissingCache(String name, LayeringCacheSetting layeringCacheSetting) {
        // 创建一级缓存
        CaffeineCache caffeineCache = new CaffeineCache(name, isStats(), layeringCacheSetting);
        return caffeineCache;
    }

    @Override
    public List<CacheStatsInfo> listCacheStats() {
        List<CacheStatsInfo> cacheStatsInfoList = new ArrayList<>();
        Collection<String> cacheNames = this.getCacheNames();
        for (String cacheName : cacheNames) {
            List<CacheStatsInfo> cacheStatsInfos = listCacheStats(cacheName);
            if (!cacheStatsInfos.isEmpty()) {
                cacheStatsInfoList.addAll(cacheStatsInfos);
            }
        }
        return cacheStatsInfoList;
    }

    @Override
    public List<CacheStatsInfo> listCacheStats(String cacheName) {
        List<CacheStatsInfo> cacheStatsInfoList = new ArrayList<>();

        Collection<Cache> cacheCollection = this.getCache(cacheName);
        for (Cache cache : cacheCollection) {
            CaffeineCache caffeineCache = (CaffeineCache) cache;
            CacheStatsInfo cacheStatsInfo = new CacheStatsInfo();
            cacheStatsInfo.setCacheName(cacheName);
            cacheStatsInfo.setInternalKey(caffeineCache.getLayeringCacheSetting().getInternalKey());
            cacheStatsInfo.setDepict(caffeineCache.getLayeringCacheSetting().getDepict());
            cacheStatsInfo.setLayeringCacheSetting(caffeineCache.getLayeringCacheSetting());
            CacheStats cacheStats = caffeineCache.getCacheStats();
            cacheStats.getAndResetCachedMethodRequestTime();
            cacheStatsInfo.setRequestCount(cacheStatsInfo.getRequestCount() + cacheStats.getAndResetCacheRequestCount());
            cacheStatsInfo.setMissCount(cacheStatsInfo.getMissCount() + cacheStats.getAndResetCachedMethodRequestCount());
            cacheStatsInfo.setTotalLoadTime(cacheStatsInfo.getTotalLoadTime() + cacheStats.getAndResetCachedMethodRequestTime());
            cacheStatsInfo.setHitRate((cacheStatsInfo.getRequestCount() - cacheStatsInfo.getMissCount()) / (double) cacheStatsInfo.getRequestCount() * 100);
            cacheStatsInfo.setFirstCacheRequestCount(cacheStatsInfo.getFirstCacheRequestCount() + cacheStats.getAndResetCacheRequestCount());
            cacheStatsInfo.setFirstCacheMissCount(cacheStatsInfo.getFirstCacheMissCount() + cacheStats.getAndResetCachedMethodRequestCount());
            // 清空加载缓存时间
            cacheStatsInfoList.add(cacheStatsInfo);
        }

        return cacheStatsInfoList;
    }

    @Override
    public void resetCacheStat() {
        Collection<String> cacheNames = this.getCacheNames();
        cacheNames.stream().forEach(cacheName -> {
            resetCacheStat(cacheName);
        });
    }

    @Override
    public void resetCacheStat(String cacheName) {
        Collection<Cache> cacheCollection = this.getCache(cacheName);
        for (Cache cache : cacheCollection) {
            CaffeineCache caffeineCache = (CaffeineCache) cache;
            caffeineCache.setCacheStats(new CacheStats());
        }
    }

}
