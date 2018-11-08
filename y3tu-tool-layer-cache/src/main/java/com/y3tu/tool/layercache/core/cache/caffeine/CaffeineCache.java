package com.y3tu.tool.layercache.core.cache.caffeine;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.y3tu.tool.core.date.DateUtil;
import com.y3tu.tool.core.date.TimeInterval;
import com.y3tu.tool.layercache.core.cache.AbstractValueAdaptingCache;
import com.y3tu.tool.layercache.core.setting.FirstCacheSetting;
import com.y3tu.tool.layercache.core.support.ExpireMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;

/**
 * 基于Caffeine实现的一级缓存
 *
 * @author yuhao.wang
 */
@Slf4j
public class CaffeineCache extends AbstractValueAdaptingCache {

    /**
     * 缓存对象
     */
    private final Cache<Object, Object> cache;

    /**
     * 使用name和{@link FirstCacheSetting}创建一个 {@link CaffeineCache} 实例
     *
     * @param name              缓存名称
     * @param firstCacheSetting 一级缓存配置 {@link FirstCacheSetting}
     * @param stats             是否开启统计模式
     */
    public CaffeineCache(String name, FirstCacheSetting firstCacheSetting, boolean stats) {
        this(name, getCache(firstCacheSetting), true, stats);
    }

    /**
     * 使用name和{@link Cache}创建一个 {@link CaffeineCache} 实例
     *
     * @param name            缓存名称
     * @param cache           t一个 Caffeine Cache 的实例对象
     * @param allowNullValues 缓存是否允许存NULL（true：允许）
     * @param stats           是否开启统计模式
     */
    public CaffeineCache(String name, Cache<Object, Object> cache,
                         boolean allowNullValues, boolean stats) {

        super(allowNullValues, stats, name);
        Assert.notNull(cache, "Cache 不能为NULL");
        this.cache = cache;
    }

    @Override
    public Cache<Object, Object> getNativeCache() {
        return this.cache;
    }

    @Override
    public Object get(Object key) {
        log.debug("caffeine缓存 key={} 获取缓存", JSON.toJSONString(key));

        if (isStats()) {
            //调用缓存请求次数+1
            getCacheStats().addCacheRequestCount(1);
        }

        if (this.cache instanceof LoadingCache) {
            return ((LoadingCache<Object, Object>) this.cache).get(key);
        }
        return cache.getIfPresent(key);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        log.debug("caffeine缓存 key={} 获取缓存， 如果没有命中就走库加载缓存", JSON.toJSONString(key));

        if (isStats()) {
            //调用缓存请求次数+1
            getCacheStats().addCacheRequestCount(1);
        }

        Object result = this.cache.get(key, (k) -> loaderValue(key, valueLoader));
        return (T) fromStoreValue(result);
    }

    @Override
    public void put(Object key, Object value) {
        log.debug("caffeine缓存 key={} put缓存，缓存值：{}", JSON.toJSONString(key), JSON.toJSONString(value));
        this.cache.put(key, toStoreValue(value));
    }

    @Override
    public Object putIfAbsent(Object key, Object value) {
        log.debug("caffeine缓存 key={} putIfAbsent 缓存，缓存值：{}", JSON.toJSONString(key), JSON.toJSONString(value));
        Object result = this.cache.get(key, k -> toStoreValue(value));
        return fromStoreValue(result);
    }

    @Override
    public void evict(Object key) {
        log.debug("caffeine缓存 key={} 清除缓存", JSON.toJSONString(key));
        this.cache.invalidate(key);
    }

    @Override
    public void clear() {
        log.debug("caffeine缓存 key={} 清空缓存");
        this.cache.invalidateAll();
    }

    /**
     * 加载数据
     */
    private <T> Object loaderValue(Object key, Callable<T> valueLoader) {
        TimeInterval timer = DateUtil.timer();

        if (isStats()) {
            //调用被缓存的方法次数+1
            getCacheStats().addCachedMethodRequestCount(1);
        }

        try {
            //调用被缓存的方法
            T t = valueLoader.call();
            log.debug("caffeine缓存 key={} 从库加载缓存", JSON.toJSONString(key), JSON.toJSONString(t));

            if (isStats()) {
                getCacheStats().addCachedMethodRequestTime(timer.intervalMs());
            }
            return toStoreValue(t);
        } catch (Exception e) {
            log.error("加载缓存数据异常,{}", e.getMessage(), e);
            throw new LoaderCacheValueException(key, e);
        }

    }

    /**
     * 根据配置获取本地缓存对象
     *
     * @param firstCacheSetting 一级缓存配置
     * @return {@link Cache}
     */
    private static Cache<Object, Object> getCache(FirstCacheSetting firstCacheSetting) {
        // 根据配置创建Caffeine builder
        Caffeine<Object, Object> builder = Caffeine.newBuilder();
        builder.initialCapacity(firstCacheSetting.getInitialCapacity());
        builder.maximumSize(firstCacheSetting.getMaximumSize());
        if (ExpireMode.WRITE.equals(firstCacheSetting.getExpireMode())) {
            builder.expireAfterWrite(firstCacheSetting.getExpireTime(), firstCacheSetting.getTimeUnit());
        } else if (ExpireMode.ACCESS.equals(firstCacheSetting.getExpireMode())) {
            builder.expireAfterAccess(firstCacheSetting.getExpireTime(), firstCacheSetting.getTimeUnit());
        }
        // 根据Caffeine builder创建 Cache 对象
        return builder.build();
    }
}
