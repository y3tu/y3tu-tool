package com.y3tu.tool.cache.core.stats;

import com.alibaba.fastjson.JSON;
import com.y3tu.tool.cache.redis.lock.Lock;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.LayerCache;
import com.y3tu.tool.cache.core.manager.AbstractCacheManager;
import com.y3tu.tool.cache.core.manager.CacheManager;
import com.y3tu.tool.cache.core.setting.LayerCacheSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 统计服务
 *
 * @author yuhao.wang3
 */
@Slf4j
public class StatsService {

    /**
     * 缓存统计数据前缀
     */
    private static final String CACHE_STATS_KEY_PREFIX = "layering-cache:cache_stats_info:y3tu:";

    /**
     * 定时任务线程池
     */
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(50);

    /**
     * {@link AbstractCacheManager }
     */
    private AbstractCacheManager cacheManager;

    /**
     * 获取缓存统计list
     *
     * @param cacheNameParam 缓存名称
     * @return List&lt;CacheStatsInfo&gt;
     */
    public List<CacheStatsInfo> listCacheStats(String cacheNameParam) {
        log.debug("获取缓存统计数据");

        List<CacheStatsInfo> statsList = new ArrayList<>();
        Collection<String> cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            if (StrUtil.isNotBlank(cacheNameParam) && !cacheName.startsWith(cacheNameParam)) {
                continue;
            }

            // 获取Cache
            Collection<Cache> caches = cacheManager.getCache(cacheName);
            for (Cache cache : caches) {
                LayerCache layerCache = (LayerCache) cache;
                LayerCacheSetting layerCacheSetting = layerCache.getLayerCacheSetting();
                // 加锁并增量缓存统计数据，缓存key=固定前缀 + 缓存名称加 + 内部缓存名
                String redisKey = CACHE_STATS_KEY_PREFIX + cacheName + layerCacheSetting.getInternalKey();
                CacheStatsInfo cacheStats = (CacheStatsInfo) cacheManager.getRedisTemplate().opsForValue().get(redisKey);
                if (!Objects.isNull(cacheStats)) {
                    statsList.add(cacheStats);
                }
            }
        }

        return statsList.stream().sorted(Comparator.comparing(CacheStatsInfo::getHitRate)).collect(Collectors.toList());
    }

    /**
     * 同步缓存统计list
     */
    public void syncCacheStats() {
        RedisTemplate<String, Object> redisTemplate = cacheManager.getRedisTemplate();
        // 清空统计数据
        resetCacheStat();
        executor.scheduleWithFixedDelay(() -> {
            log.debug("执行缓存统计数据采集定时任务");
            Set<AbstractCacheManager> cacheManagers = AbstractCacheManager.getCacheManager();
            for (AbstractCacheManager abstractCacheManager : cacheManagers) {
                // 获取CacheManager
                CacheManager cacheManager = ((CacheManager) abstractCacheManager);
                Collection<String> cacheNames = cacheManager.getCacheNames();
                for (String cacheName : cacheNames) {
                    // 获取Cache
                    Collection<Cache> caches = cacheManager.getCache(cacheName);
                    for (Cache cache : caches) {
                        LayerCache layerCache = (LayerCache) cache;
                        LayerCacheSetting layerCacheSetting = layerCache.getLayerCacheSetting();
                        // 加锁并增量缓存统计数据，缓存key=固定前缀 +缓存名称加 + 内部缓存名
                        String redisKey = CACHE_STATS_KEY_PREFIX + cacheName + layerCacheSetting.getInternalKey();
                        Lock lock = new Lock(redisTemplate, redisKey, 60, 5000);
                        try {
                            if (lock.tryLock()) {
                                CacheStatsInfo cacheStats = (CacheStatsInfo) redisTemplate.opsForValue().get(redisKey);
                                if (Objects.isNull(cacheStats)) {
                                    cacheStats = new CacheStatsInfo();
                                }

                                // 设置缓存唯一标示
                                cacheStats.setCacheName(cacheName);
                                cacheStats.setInternalKey(layerCacheSetting.getInternalKey());

                                cacheStats.setDepict(layerCacheSetting.getDepict());
                                // 设置缓存配置信息
                                cacheStats.setLayerCacheSetting(layerCacheSetting);

                                // 设置缓存统计数据
                                CacheStats layeringCacheStats = layerCache.getCacheStats();
                                CacheStats firstCacheStats = layerCache.getFirstCache().getCacheStats();
                                CacheStats secondCacheStats = layerCache.getSecondCache().getCacheStats();

                                // 清空加载缓存时间
                                firstCacheStats.getAndResetCachedMethodRequestTime();
                                secondCacheStats.getAndResetCachedMethodRequestTime();

                                cacheStats.setRequestCount(cacheStats.getRequestCount() + layeringCacheStats.getAndResetCacheRequestCount());
                                cacheStats.setMissCount(cacheStats.getMissCount() + layeringCacheStats.getAndResetCachedMethodRequestCount());
                                cacheStats.setTotalLoadTime(cacheStats.getTotalLoadTime() + layeringCacheStats.getAndResetCachedMethodRequestTime());
                                cacheStats.setHitRate((cacheStats.getRequestCount() - cacheStats.getMissCount()) / (double) cacheStats.getRequestCount() * 100);

                                cacheStats.setFirstCacheRequestCount(cacheStats.getFirstCacheRequestCount() + firstCacheStats.getAndResetCacheRequestCount());
                                cacheStats.setFirstCacheMissCount(cacheStats.getFirstCacheMissCount() + firstCacheStats.getAndResetCachedMethodRequestCount());

                                cacheStats.setSecondCacheRequestCount(cacheStats.getSecondCacheRequestCount() + secondCacheStats.getAndResetCacheRequestCount());
                                cacheStats.setSecondCacheMissCount(cacheStats.getSecondCacheMissCount() + secondCacheStats.getAndResetCachedMethodRequestCount());

                                // 将缓存统计数据写到redis
                                redisTemplate.opsForValue().set(redisKey, cacheStats, 1, TimeUnit.HOURS);

                                log.info("Layering Cache 统计信息：{}", JSON.toJSONString(cacheStats));
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        } finally {
                            lock.unlock();
                        }
                    }
                }
            }

            //  初始时间间隔是1分
        }, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * 关闭线程池
     */
    public void shutdownExecutor() {
        executor.shutdown();
    }

    /**
     * 重置缓存统计数据
     */
    public void resetCacheStat() {
        RedisTemplate<String, Object> redisTemplate = cacheManager.getRedisTemplate();
        Set<String> keys = redisTemplate.keys(CACHE_STATS_KEY_PREFIX + "*");
        redisTemplate.delete(keys);
    }

    public void setCacheManager(AbstractCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
