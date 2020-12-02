package com.y3tu.tool.cache.core.stats;

import com.alibaba.fastjson.JSON;
import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.LayeringCache;
import com.y3tu.tool.cache.core.manager.AbstractCacheManager;
import com.y3tu.tool.cache.core.manager.SecondaryCacheManager;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import com.y3tu.tool.cache.redis.service.RedisService;
import com.y3tu.tool.cache.redis.support.Lock;
import com.y3tu.tool.core.thread.ThreadUtil;
import com.y3tu.tool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 统计服务 依赖redis
 *
 * @author yuhao.wang3
 */
@Slf4j
public class SecondaryStatsService {

    /**
     * 缓存统计数据前缀
     */
    public static final String CACHE_STATS_KEY_PREFIX = "cache:cache_stats_info:tool:";

    /**
     * 定时任务线程池
     */
    private static ScheduledExecutorService executor = ThreadUtil.newScheduledExecutor(50, ThreadUtil.newNamedThreadFactory("缓存统计服务线程池", true));

    /**
     * {@link AbstractCacheManager }
     */
    private SecondaryCacheManager cacheManager;

    /**
     * 获取缓存统计list
     *
     * @param cacheNameParam 缓存名称
     * @return List&lt;CacheStatsInfo&gt;
     */
    public List<CacheStatsInfo> listCacheStats(String cacheNameParam) {
        log.debug("获取缓存统计数据");

        Set<String> layeringCacheKeys = new RedisService(cacheManager.getRedisTemplate()).scan(CACHE_STATS_KEY_PREFIX + "*");
        if (CollectionUtils.isEmpty(layeringCacheKeys)) {
            return Collections.emptyList();
        }
        // 遍历找出对应统计数据
        List<CacheStatsInfo> statsList = new ArrayList<>();
        for (String key : layeringCacheKeys) {
            if (StrUtil.isNotBlank(cacheNameParam) && !key.startsWith(CACHE_STATS_KEY_PREFIX + cacheNameParam)) {
                continue;
            }

            CacheStatsInfo cacheStats = (CacheStatsInfo) cacheManager.getRedisTemplate().opsForValue().get(key);
            if (!Objects.isNull(cacheStats)) {
                statsList.add(cacheStats);
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

        ThreadUtil.scheduleWithFixedDelay(executor, () -> {
            log.debug("执行缓存统计数据采集定时任务");
            // 获取CacheManager
            Collection<String> cacheNames = cacheManager.getCacheNames();
            for (String cacheName : cacheNames) {
                // 获取Cache
                Collection<Cache> caches = cacheManager.getCache(cacheName);
                for (Cache cache : caches) {
                    LayeringCache layeringCache = (LayeringCache) cache;
                    LayeringCacheSetting layeringCacheSetting = layeringCache.getLayeringCacheSetting();
                    // 加锁并增量缓存统计数据，缓存key=固定前缀 +缓存名称加 + 内部缓存名
                    String redisKey = CACHE_STATS_KEY_PREFIX + cacheName + layeringCacheSetting.getInternalKey();
                    Lock lock = new Lock(redisTemplate, redisKey, 60, 5000);
                    try {
                        if (lock.tryLock()) {
                            CacheStatsInfo cacheStats = (CacheStatsInfo) redisTemplate.opsForValue().get(redisKey);
                            if (Objects.isNull(cacheStats)) {
                                cacheStats = new CacheStatsInfo();
                            }

                            // 设置缓存唯一标示
                            cacheStats.setCacheName(cacheName);
                            cacheStats.setInternalKey(layeringCacheSetting.getInternalKey());

                            cacheStats.setDepict(layeringCacheSetting.getDepict());
                            // 设置缓存配置信息
                            cacheStats.setLayeringCacheSetting(layeringCacheSetting);

                            // 设置缓存统计数据
                            CacheStats layeringCacheStats = layeringCache.getCacheStats();
                            CacheStats firstCacheStats = layeringCache.getFirstCache().getCacheStats();
                            CacheStats secondCacheStats = layeringCache.getSecondCache().getCacheStats();

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
                            redisTemplate.opsForValue().set(redisKey, cacheStats, 24, TimeUnit.HOURS);

                            log.info("Layering Cache 统计信息：{}", JSON.toJSONString(cacheStats));
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        lock.unlock();
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
        Set<String> layeringCacheKeys = new RedisService(cacheManager.getRedisTemplate()).scan(CACHE_STATS_KEY_PREFIX + "*");

        for (String key : layeringCacheKeys) {
            resetCacheStat(key);
        }
    }

    /**
     * 重置缓存统计数据
     *
     * @param redisKey redisKey
     */
    public void resetCacheStat(String redisKey) {
        RedisTemplate<String, Object> redisTemplate = cacheManager.getRedisTemplate();
        try {
            CacheStatsInfo cacheStats = (CacheStatsInfo) redisTemplate.opsForValue().get(redisKey);
            if (Objects.nonNull(cacheStats)) {
                cacheStats.clearStatsInfo();
                // 将缓存统计数据写到redis
                redisTemplate.opsForValue().set(redisKey, cacheStats, 24, TimeUnit.HOURS);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void setCacheManager(SecondaryCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
