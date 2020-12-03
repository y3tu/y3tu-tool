package com.y3tu.tool.cache.core.manager;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

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
     * 外层key是cache_name
     * 里层key是[一级缓存有效时间-二级缓存有效时间-二级缓存自动刷新时间]
     */
    public final ConcurrentMap<String, ConcurrentMap<String, Cache>> cacheContainer = new ConcurrentHashMap<>(16);

    /**
     * 缓存名称容器
     */
    public volatile Set<String> cacheNames = new LinkedHashSet<>();

    /**
     * 是否开启统计
     */
    public boolean stats = false;


    @Override
    public Collection<Cache> getCache(String name) {
        ConcurrentMap<String, Cache> cacheMap = this.cacheContainer.get(name);
        if (CollectionUtils.isEmpty(cacheMap)) {
            return Collections.emptyList();
        }
        return cacheMap.values();
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

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheNames;
    }

    /**
     * 更新缓存名称容器
     *
     * @param name 需要添加的缓存名称
     */
    public void updateCacheNames(String name) {
        cacheNames.add(name);
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
    public ConcurrentMap<String, ConcurrentMap<String, Cache>> getCacheContainer() {
        return cacheContainer;
    }

    @Override
    public void clearCache(String cacheName) {
        Collection<Cache> caches = getCache(cacheName);
        for (Cache cache : caches) {
            cache.clear();
        }
    }

    @Override
    public void clearCache() {
        Collection<String> cacheNames = getCacheNames();
        cacheNames.stream().forEach(cacheName -> {
            clearCache(cacheName);
        });
    }

}
