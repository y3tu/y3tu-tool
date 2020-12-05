package com.y3tu.tool.cache.core.manager;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 公共的抽象 {@link CacheManager} 的实现.
 * 缓存管理
 *
 * @author yuhao.wang3
 */
@Slf4j
@Data
public abstract class AbstractCacheManager implements CacheManager {

    /**
     * 缓存容器
     * key是cache_name
     */
    public final ConcurrentMap<String, Cache> cacheContainer = new ConcurrentHashMap<>(16);

    /**
     * 缓存名称容器
     */
    public volatile Set<String> cacheNames = new LinkedHashSet<>();

    /**
     * 是否开启统计
     */
    public boolean stats = false;


    @Override
    public Cache getCache(String name) {
        Cache cache = this.cacheContainer.get(name);
        return cache;
    }

    @Override
    public Cache getCache(String cacheName, LayeringCacheSetting layeringCacheSetting) {
        // 第一次获取缓存Cache，如果有直接返回,如果没有加锁往容器里里面放Cache
        Cache cache = this.cacheContainer.get(cacheName);
        if (cache != null) {
            return cache;
        }
        // 第二次获取缓存Cache，加锁往容器里里面放Cache
        synchronized (this.cacheContainer) {
            // 新建一个Cache对象
            cache = getMissingCache(cacheName, layeringCacheSetting);
            if (cache != null) {
                // 装饰Cache对象
                cache = decorateCache(cache);
                // 将新的Cache对象放到容器
                this.cacheContainer.put(cacheName, cache);
            }
            //更新缓存名称
            updateCacheName(cacheName);
            return cache;
        }
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheNames;
    }

    /**
     * 增加缓存名称容器
     *
     * @param cacheName 需要添加的缓存名称
     */
    public void updateCacheName(String cacheName) {
        cacheNames.add(cacheName);
    }

    /**
     * 获取Cache对象的装饰示例
     *
     * @param cache 需要添加到CacheManager的Cache实例
     * @return 装饰过后的Cache实例
     */
    protected Cache decorateCache(Cache cache) {
        return cache;
    }

    /**
     * 根据缓存名称在CacheManager中没有找到对应Cache时，通过该方法新建一个对应的Cache实例
     *
     * @param name                 缓存名称
     * @param layeringCacheSetting 缓存配置
     * @return {@link Cache}
     */
    protected abstract Cache getMissingCache(String name, LayeringCacheSetting layeringCacheSetting);

    /**
     * 获取缓存容器
     *
     * @return 返回缓存容器
     */
    public ConcurrentMap<String, Cache> getCacheContainer() {
        return cacheContainer;
    }

    @Override
    public void clearCache(String cacheName) {
        Cache cache = getCache(cacheName);
        cache.clear();
    }

    @Override
    public void clearCache() {
        Collection<String> cacheNames = getCacheNames();
        cacheNames.stream().forEach(cacheName -> {
            clearCache(cacheName);
        });
    }

}
