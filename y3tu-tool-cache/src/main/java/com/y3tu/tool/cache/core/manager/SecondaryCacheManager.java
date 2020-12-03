package com.y3tu.tool.cache.core.manager;

import com.alibaba.fastjson.JSON;
import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.redis.RedisCache;
import com.y3tu.tool.cache.core.listener.RedisMessageListener;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import com.y3tu.tool.cache.core.stats.CacheStats;
import com.y3tu.tool.cache.core.stats.CacheStatsInfo;
import com.y3tu.tool.cache.redis.service.RedisService;
import com.y3tu.tool.cache.redis.support.Lock;
import com.y3tu.tool.core.thread.ThreadUtil;
import com.y3tu.tool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
public class SecondaryCacheManager extends AbstractCacheManager implements InitializingBean, DisposableBean, BeanNameAware, SmartLifecycle {

    /**
     * 缓存统计数据前缀
     */
    public static final String CACHE_STATS_KEY_PREFIX = "cache:cache_stats_info:tool:";
    /**
     * redis pub/sub 容器
     */
    private final RedisMessageListenerContainer container = new RedisMessageListenerContainer();

    /**
     * redis pub/sub 监听器
     */
    private final RedisMessageListener messageListener = new RedisMessageListener();

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
    public Cache getCache(String name, LayeringCacheSetting layeringCacheSetting) {
        // 第一次获取缓存Cache，如果有直接返回,如果没有加锁往容器里里面放Cache
        ConcurrentMap<String, Cache> cacheMap = this.cacheContainer.get(name);
        if (!CollectionUtils.isEmpty(cacheMap)) {
            if (cacheMap.size() > 1) {
                log.warn("缓存名称为 {} 的缓存,存在两个不同的过期时间配置，请一定注意保证缓存的key唯一性，否则会出现缓存过期时间错乱的情况", name);
            }
            Cache cache = cacheMap.get(layeringCacheSetting.getInternalKey());
            if (cache != null) {
                return cache;
            }
        }

        // 第二次获取缓存Cache，加锁往容器里里面放Cache
        synchronized (this.cacheContainer) {
            cacheMap = this.cacheContainer.get(name);
            if (!CollectionUtils.isEmpty(cacheMap)) {
                // 从容器中获取缓存
                Cache cache = cacheMap.get(layeringCacheSetting.getInternalKey());
                if (cache != null) {
                    return cache;
                }
            } else {
                cacheMap = new ConcurrentHashMap<>(16);
                cacheContainer.put(name, cacheMap);
                // 更新缓存名称
                updateCacheNames(name);
                // 创建redis监听
                addMessageListener(name);
            }

            // 新建一个Cache对象
            Cache cache = getMissingCache(name, layeringCacheSetting);
            if (cache != null) {
                // 装饰Cache对象
                cache = decorateCache(cache);
                // 将新的Cache对象放到容器
                cacheMap.put(layeringCacheSetting.getInternalKey(), cache);
                if (cacheMap.size() > 1) {
                    log.warn("缓存名称为 {} 的缓存,存在两个不同的过期时间配置，请一定注意保证缓存的key唯一性，否则会出现缓存过期时间错乱的情况", name);
                }
            }

            return cache;
        }
    }

    /**
     * 添加消息监听
     *
     * @param name 缓存名称
     */
    protected void addMessageListener(String name) {
        container.addMessageListener(messageListener, new ChannelTopic(name));
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
            resetCacheStat(key);
        }
    }

    @Override
    public void resetCacheStat(String cacheName) {
        RedisTemplate<String, Object> redisTemplate = this.getRedisTemplate();
        try {
            CacheStatsInfo cacheStats = (CacheStatsInfo) redisTemplate.opsForValue().get(CACHE_STATS_KEY_PREFIX + cacheName);
            if (Objects.nonNull(cacheStats)) {
                cacheStats.clearStatsInfo();
                // 将缓存统计数据写到redis
                redisTemplate.opsForValue().set(CACHE_STATS_KEY_PREFIX + cacheName, cacheStats, 24, TimeUnit.HOURS);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        messageListener.setCacheManager(this);
        container.setConnectionFactory(getRedisTemplate().getConnectionFactory());
        container.afterPropertiesSet();
        messageListener.afterPropertiesSet();
    }

    @Override
    public void setBeanName(String name) {
        container.setBeanName("redisMessageListenerContainer");
    }

    @Override
    public void destroy() throws Exception {
        container.destroy();
        if (executor != null) {
            executor.shutdown();
        }
    }

    @Override
    public boolean isAutoStartup() {
        return container.isAutoStartup();
    }

    @Override
    public void stop(Runnable callback) {
        container.stop(callback);
    }

    @Override
    public void start() {
        container.start();
    }

    @Override
    public void stop() {
        container.stop();
    }

    @Override
    public boolean isRunning() {
        return container.isRunning();
    }

    @Override
    public int getPhase() {
        return container.getPhase();
    }

    /**
     * 同步缓存统计list
     */
    public void syncCacheStats() {
        RedisTemplate<String, Object> redisTemplate = this.getRedisTemplate();
        // 清空统计数据
        resetCacheStat();
        //定时任务线程池
        executor = ThreadUtil.newScheduledExecutor(50, ThreadUtil.newNamedThreadFactory("缓存统计服务线程池", true));
        executor.scheduleAtFixedRate(() -> {
            log.debug("执行缓存统计数据采集定时任务");
            // 获取CacheManager
            Collection<String> cacheNames = this.getCacheNames();
            for (String cacheName : cacheNames) {
                // 获取Cache
                Collection<Cache> caches = this.getCache(cacheName);
                for (Cache cache : caches) {
                    RedisCache redisCache = (RedisCache) cache;
                    LayeringCacheSetting layeringCacheSetting = redisCache.getLayeringCacheSetting();
                    // 加锁并增量缓存统计数据，缓存key=固定前缀 +缓存名称加 + 内部缓存名
                    String redisKey = CACHE_STATS_KEY_PREFIX + cacheName + layeringCacheSetting.getInternalKey();
                    Lock lock = new Lock(redisTemplate, redisKey, 60, 5000);
                    try {
                        if (lock.tryLock()) {
                            CacheStatsInfo cacheStatsInfo = (CacheStatsInfo) redisTemplate.opsForValue().get(redisKey);
                            if (Objects.isNull(cacheStatsInfo)) {
                                cacheStatsInfo = new CacheStatsInfo();
                            }

                            // 设置缓存唯一标示
                            cacheStatsInfo.setCacheName(cacheName);
                            cacheStatsInfo.setInternalKey(layeringCacheSetting.getInternalKey());
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
}
