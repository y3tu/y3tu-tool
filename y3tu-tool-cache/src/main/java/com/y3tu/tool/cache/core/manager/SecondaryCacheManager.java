package com.y3tu.tool.cache.core.manager;

import com.alibaba.fastjson.JSON;
import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.redis.RedisCache;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import com.y3tu.tool.cache.core.stats.CacheStats;
import com.y3tu.tool.cache.core.stats.CacheStatsInfo;
import com.y3tu.tool.cache.redis.service.RedisService;
import com.y3tu.tool.cache.redis.support.Lock;
import com.y3tu.tool.core.thread.ThreadUtil;
import com.y3tu.tool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 二级缓存管理
 *
 * @author y3tu
 */
@Slf4j
@Data
public class SecondaryCacheManager extends AbstractCacheManager implements DisposableBean {

    /**
     * 缓存统计数据前缀
     */
    public static final String CACHE_STATS_KEY_PREFIX = "cache:cache_stats_info:tool:";

    /**
     * redis操作
     */
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 同步缓存统计线程池
     */
    ScheduledExecutorService executor;

    public SecondaryCacheManager() {

    }

    public SecondaryCacheManager(RedisTemplate<String, Object> redisTemplate, boolean stats) {
        this.redisTemplate = redisTemplate;
        this.stats = stats;
        if (stats) {
            syncCacheStats();
        }
    }

    @Override
    protected Cache getMissingCache(String name, LayeringCacheSetting layeringCacheSetting) {
        // 创建二级缓存
        RedisCache redisCache = new RedisCache(name, isStats(), redisTemplate, layeringCacheSetting);
        return redisCache;
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
        log.debug("获取缓存统计数据");

        Set<String> layeringCacheKeys = new RedisService(this.getRedisTemplate()).scan(CACHE_STATS_KEY_PREFIX + "*");
        if (CollectionUtils.isEmpty(layeringCacheKeys)) {
            return Collections.emptyList();
        }
        // 遍历找出对应统计数据
        List<CacheStatsInfo> statsList = new ArrayList<>();
        for (String key : layeringCacheKeys) {
            if (StrUtil.isNotBlank(cacheName) && !key.startsWith(CACHE_STATS_KEY_PREFIX + cacheName)) {
                continue;
            }

            CacheStatsInfo cacheStats = (CacheStatsInfo) this.getRedisTemplate().opsForValue().get(key);
            if (!Objects.isNull(cacheStats)) {
                statsList.add(cacheStats);
            }
        }

        return statsList.stream().sorted(Comparator.comparing(CacheStatsInfo::getHitRate)).collect(Collectors.toList());
    }

    @Override
    public void resetCacheStat() {
        RedisTemplate<String, Object> redisTemplate = this.getRedisTemplate();
        Set<String> layeringCacheKeys = new RedisService(this.getRedisTemplate()).scan(CACHE_STATS_KEY_PREFIX + "*");
        for (String key : layeringCacheKeys) {
            resetCacheStatByKey(key);
        }
    }

    private void resetCacheStatByKey(String key) {
        RedisTemplate<String, Object> redisTemplate = this.getRedisTemplate();
        try {
            CacheStatsInfo cacheStats = (CacheStatsInfo) redisTemplate.opsForValue().get(key);
            if (Objects.nonNull(cacheStats)) {
                cacheStats.clearStatsInfo();
                // 将缓存统计数据写到redis
                redisTemplate.opsForValue().set(key, cacheStats, 24, TimeUnit.HOURS);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void resetCacheStat(String cacheName) {
        String key = CACHE_STATS_KEY_PREFIX + cacheName;
        resetCacheStatByKey(key);
    }

    @Override
    public void destroy() throws Exception {
        if (executor != null) {
            executor.shutdown();
        }
    }

    @Override
    public void clearCache(String cacheName) {
        Cache cache = getCache(cacheName);
        //删除缓存统计
        RedisCache redisCache = (RedisCache) cache;
        LayeringCacheSetting layeringCacheSetting = redisCache.getLayeringCacheSetting();
        String redisKey = CACHE_STATS_KEY_PREFIX + cacheName;
        redisTemplate.delete(redisKey);
        //清空缓存
        cache.clear();
    }

    /**
     * 同步缓存统计list
     */
    public void syncCacheStats() {
        RedisTemplate<String, Object> redisTemplate = this.getRedisTemplate();
        // 清空统计数据
        resetCacheStat();
        //定时任务线程池
        executor = ThreadUtil.newScheduledExecutor(1, ThreadUtil.newNamedThreadFactory("缓存统计服务线程池", true));
        executor.scheduleAtFixedRate(() -> {
            log.debug("执行缓存统计数据采集定时任务");
            // 获取CacheManager
            Collection<String> cacheNames = this.getCacheNames();
            for (String cacheName : cacheNames) {
                // 获取Cache
                Cache cache = this.getCache(cacheName);
                RedisCache redisCache = (RedisCache) cache;
                LayeringCacheSetting layeringCacheSetting = redisCache.getLayeringCacheSetting();
                // 加锁并增量缓存统计数据，缓存key=固定前缀 +缓存名称
                String redisKey = CACHE_STATS_KEY_PREFIX + cacheName;
                Lock lock = new Lock(redisTemplate, redisKey, 60, 5000);
                try {
                    if (lock.tryLock()) {
                        CacheStatsInfo cacheStatsInfo = (CacheStatsInfo) redisTemplate.opsForValue().get(redisKey);
                        if (Objects.isNull(cacheStatsInfo)) {
                            cacheStatsInfo = new CacheStatsInfo();
                        }

                        // 设置缓存唯一标示
                        cacheStatsInfo.setCacheName(cacheName);
                        cacheStatsInfo.setDepict(layeringCacheSetting.getDepict());
                        // 设置缓存配置信息
                        cacheStatsInfo.setLayeringCacheSetting(layeringCacheSetting);
                        // 设置缓存统计数据
                        CacheStats cacheStats = redisCache.getCacheStats();


                        // 清空加载缓存时间
                        cacheStats.getAndResetCachedMethodRequestTime();

                        cacheStatsInfo.setRequestCount(cacheStatsInfo.getRequestCount() + cacheStats.getAndResetCacheRequestCount());
                        cacheStatsInfo.setMissCount(cacheStatsInfo.getMissCount() + cacheStats.getAndResetCachedMethodRequestCount());
                        cacheStatsInfo.setTotalLoadTime(cacheStatsInfo.getTotalLoadTime() + cacheStats.getAndResetCachedMethodRequestTime());
                        cacheStatsInfo.setHitRate((cacheStatsInfo.getRequestCount() - cacheStatsInfo.getMissCount()) / (double) cacheStatsInfo.getRequestCount() * 100);
                        cacheStatsInfo.setFirstCacheRequestCount(cacheStatsInfo.getFirstCacheRequestCount() + cacheStats.getAndResetCacheRequestCount());
                        cacheStatsInfo.setFirstCacheMissCount(cacheStatsInfo.getFirstCacheMissCount() + cacheStats.getAndResetCachedMethodRequestCount());
                        cacheStatsInfo.setSecondCacheRequestCount(cacheStatsInfo.getSecondCacheRequestCount() + cacheStats.getAndResetCacheRequestCount());
                        cacheStatsInfo.setSecondCacheMissCount(cacheStatsInfo.getSecondCacheMissCount() + cacheStats.getAndResetCachedMethodRequestCount());

                        // 将缓存统计数据写到redis
                        redisTemplate.opsForValue().set(redisKey, cacheStatsInfo, 24, TimeUnit.HOURS);

                        log.info("Layering Cache 统计信息：{}", JSON.toJSONString(cacheStatsInfo));
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    lock.unlock();
                }
            }

            //  初始时间间隔是1分
        }, 1, 1, TimeUnit.MINUTES);
    }
}
