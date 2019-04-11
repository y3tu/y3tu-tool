package com.y3tu.tool.cache.core.cache;

import com.y3tu.tool.cache.enums.CacheMode;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.cache.core.listener.RedisPubSubMessage;
import com.y3tu.tool.cache.core.listener.RedisPubSubMessageType;
import com.y3tu.tool.cache.core.listener.RedisPublisher;
import com.y3tu.tool.cache.core.setting.LayerCacheSetting;
import com.y3tu.tool.cache.core.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.concurrent.Callable;

/**
 * 多级缓存
 *
 * @author yuhao.wang
 */
@Slf4j
public class LayerCache extends AbstractValueAdaptingCache {

    /**
     * redis 客户端
     */
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 一级缓存
     */
    private Cache firstCache;

    /**
     * 二级缓存
     */
    private Cache secondCache;
    /**
     * 缓存模式
     */
    private CacheMode cacheMode;
    /**
     * 多级缓存配置
     */
    private LayerCacheSetting layerCacheSetting;

    /**
     * 创建一个多级缓存对象
     *
     * @param redisTemplate     redisTemplate
     * @param firstCache        一级缓存
     * @param secondCache       二级缓存
     * @param stats             是否开启统计
     * @param layerCacheSetting 多级缓存配置
     */
    public LayerCache(RedisTemplate<String, Object> redisTemplate, Cache firstCache, Cache secondCache, boolean stats, LayerCacheSetting layerCacheSetting) {
        this(redisTemplate, firstCache, secondCache, layerCacheSetting.getCacheMode(), stats, secondCache.getName(), layerCacheSetting);
    }

    /**
     * @param redisTemplate     redisTemplate
     * @param firstCache        一级缓存
     * @param secondCache       二级缓存
     * @param cacheMode         缓存模式
     * @param stats             是否开启统计，默认否
     * @param name              缓存名称
     * @param layerCacheSetting 多级缓存配置
     */
    public LayerCache(RedisTemplate<String, Object> redisTemplate, Cache firstCache, Cache secondCache, CacheMode cacheMode, boolean stats, String name, LayerCacheSetting layerCacheSetting) {
        super(true, stats, name);
        this.redisTemplate = redisTemplate;
        this.firstCache = firstCache;
        this.secondCache = secondCache;
        this.layerCacheSetting = layerCacheSetting;
    }

    @Override
    public LayerCache getNativeCache() {
        return this;
    }

    @Override
    public Object get(Object key) {
        Object result = null;
        if (CacheMode.ALL.equals(cacheMode)) {
            result = firstCache.get(key);
            log.debug("查询一级缓存。 key={},返回值是:{}", key, JsonUtil.toJson(result));
        }
        if (result == null) {
            result = secondCache.get(key);
            if (CacheMode.ALL.equals(cacheMode)) {
                firstCache.putIfAbsent(key, result);
                log.debug("查询二级缓存,并将数据放到一级缓存。 key={},返回值是:{}", key, JsonUtil.toJson(result));
            }
        }
        return fromStoreValue(result);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        if (CacheMode.ALL.equals(cacheMode)) {
            Object result = firstCache.get(key, type);
            log.debug("查询一级缓存。 key={},返回值是:{}", key, JsonUtil.toJson(result));
            if (result != null) {
                return (T) fromStoreValue(result);
            }
        }

        T result = secondCache.get(key, type);
        if (CacheMode.ALL.equals(cacheMode)) {
            firstCache.putIfAbsent(key, result);
        }
        log.debug("查询二级缓存,并将数据放到一级缓存。 key={},返回值是:{}", key, JsonUtil.toJson(result));
        return result;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        if (CacheMode.ALL.equals(cacheMode)) {
            Object result = firstCache.get(key);
            log.debug("查询一级缓存。 key={},返回值是:{}", key, JsonUtil.toJson(result));
            if (result != null) {
                return (T) fromStoreValue(result);
            }
        }
        T result = secondCache.get(key, valueLoader);
        if (CacheMode.ALL.equals(cacheMode)) {
            firstCache.putIfAbsent(key, result);
        }
        log.debug("查询二级缓存,并将数据放到一级缓存。 key={},返回值是:{}", key, JsonUtil.toJson(result));
        return result;
    }

    @Override
    public void put(Object key, Object value) {
        secondCache.put(key, value);
        // 删除一级缓存
        if (CacheMode.ALL.equals(cacheMode)) {
            deleteFirstCache(key);
        }
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        Object result = secondCache.putIfAbsent(key, value);
        // 删除一级缓存
        if (CacheMode.ALL.equals(cacheMode)) {
            deleteFirstCache(key);
        }
        return result;
    }

    @Override
    public void evict(Object key) {
        // 删除的时候要先删除二级缓存再删除一级缓存，否则有并发问题
        secondCache.evict(key);
        // 删除一级缓存
        if (CacheMode.ALL.equals(cacheMode)) {
            deleteFirstCache(key);
        }
    }

    @Override
    public void clear() {
        // 删除的时候要先删除二级缓存再删除一级缓存，否则有并发问题
        secondCache.clear();
        if (CacheMode.ALL.equals(cacheMode)) {
            // 清除一级缓存需要用到redis的订阅/发布模式，否则集群中其他服服务器节点的一级缓存数据无法删除
            RedisPubSubMessage message = new RedisPubSubMessage();
            message.setCacheName(getName());
            message.setMessageType(RedisPubSubMessageType.CLEAR);
            // 发布消息
            RedisPublisher.publisher(redisTemplate, new ChannelTopic(getName()), message);
        }
    }

    private void deleteFirstCache(Object key) {
        // 删除一级缓存需要用到redis的Pub/Sub（订阅/发布）模式，否则集群中其他服服务器节点的一级缓存数据无法删除
        RedisPubSubMessage message = new RedisPubSubMessage();
        message.setCacheName(getName());
        message.setKey(key);
        message.setMessageType(RedisPubSubMessageType.EVICT);
        // 发布消息
        RedisPublisher.publisher(redisTemplate, new ChannelTopic(getName()), message);
    }

    /**
     * 获取一级缓存
     *
     * @return FirstCache
     */
    public Cache getFirstCache() {
        return firstCache;
    }

    /**
     * 获取二级缓存
     *
     * @return SecondCache
     */
    public Cache getSecondCache() {
        return secondCache;
    }

    @Override
    public CacheStats getCacheStats() {
        CacheStats cacheStats = new CacheStats();
        cacheStats.addCacheRequestCount(firstCache.getCacheStats().getCacheRequestCount().longValue());
        cacheStats.addCachedMethodRequestCount(secondCache.getCacheStats().getCachedMethodRequestCount().longValue());
        cacheStats.addCachedMethodRequestTime(secondCache.getCacheStats().getCachedMethodRequestTime().longValue());

        firstCache.getCacheStats().addCachedMethodRequestCount(secondCache.getCacheStats().getCacheRequestCount().longValue());

        setCacheStats(cacheStats);
        return cacheStats;
    }

    public LayerCacheSetting getLayerCacheSetting() {
        return layerCacheSetting;
    }
}
