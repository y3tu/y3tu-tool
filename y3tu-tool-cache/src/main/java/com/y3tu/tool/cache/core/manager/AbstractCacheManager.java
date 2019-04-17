package com.y3tu.tool.cache.core.manager;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.setting.LayerCacheSetting;
import com.y3tu.tool.cache.core.stats.CacheStatsInfo;
import com.y3tu.tool.cache.core.stats.StatsService;
import com.y3tu.tool.core.bean.BeanCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 公共的抽象 {@link CacheManager} 的实现.
 *
 * @author yuhao.wang3
 */
@Slf4j
public abstract class AbstractCacheManager implements CacheManager,DisposableBean {

    /**
     * 缓存容器
     * 外层key是cache_name
     * 里层key是[一级缓存有效时间-二级缓存有效时间-二级缓存自动刷新时间]
     */
    private final ConcurrentMap<String, ConcurrentMap<String, Cache>> cacheContainer = new ConcurrentHashMap<>(16);

    /**
     * 缓存名称容器
     */
    private volatile Set<String> cacheNames = new LinkedHashSet<>();

    /**
     * CacheManager 容器
     */
    static Set<AbstractCacheManager> cacheManagers = new LinkedHashSet<>();

    /**
     * 是否开启统计 默认开启
     */
    private boolean stats = true;


    /**
     * 获取CacheManager容器
     *
     * @return
     */
    public static Set<AbstractCacheManager> getCacheManager() {
        return cacheManagers;
    }

    @Override
    public Collection<Cache> getCache(String name) {
        ConcurrentMap<String, Cache> cacheMap = this.cacheContainer.get(name);
        if (CollectionUtils.isEmpty(cacheMap)) {
            return Collections.emptyList();
        }
        return cacheMap.values();
    }

    // Lazy cache initialization on access
    @Override
    public Cache getCache(String name, LayerCacheSetting layerCacheSetting) {
        // 第一次获取缓存Cache，如果有直接返回,如果没有加锁往容器里里面放Cache
        ConcurrentMap<String, Cache> cacheMap = this.cacheContainer.get(name);
        if (!CollectionUtils.isEmpty(cacheMap)) {
            if (cacheMap.size() > 1) {
                log.warn("缓存名称为 {} 的缓存,存在两个不同的过期时间配置，请一定注意保证缓存的key唯一性，否则会出现缓存过期时间错乱的情况", name);
            }
            Cache cache = cacheMap.get(layerCacheSetting.getInternalKey());
            if (cache != null) {
                return cache;
            }
        }

        // 第二次获取缓存Cache，加锁往容器里里面放Cache
        synchronized (this.cacheContainer) {
            cacheMap = this.cacheContainer.get(name);
            if (!CollectionUtils.isEmpty(cacheMap)) {
                // 从容器中获取缓存
                Cache cache = cacheMap.get(layerCacheSetting.getInternalKey());
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
            Cache cache = getMissingCache(name, layerCacheSetting);
            if (cache != null) {
                // 装饰Cache对象
                cache = decorateCache(cache);
                // 将新的Cache对象放到容器
                cacheMap.put(layerCacheSetting.getInternalKey(), cache);
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
    private void updateCacheNames(String name) {
        cacheNames.add(name);
    }

    /**
     * 删除容器中指定的cacheName
     *
     * @param cacheName 缓存名称
     */
    @Override
    public void deleteCache(String cacheName) {
        this.cacheNames.remove(cacheName);
        this.cacheContainer.remove(cacheName);
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
     * @param name              缓存名称
     * @param layerCacheSetting 缓存配置
     * @return {@link Cache}
     */
    protected abstract Cache getMissingCache(String name, LayerCacheSetting layerCacheSetting);

    protected void addMessageListener(String name){

    }

    /**
     * 获取缓存容器
     *
     * @return 返回缓存容器
     */
    protected ConcurrentMap<String, ConcurrentMap<String, Cache>> getCacheContainer() {
        return cacheContainer;
    }



    @Override
    public List<CacheStatsInfo> listCacheStats(String cacheName) {
        return BeanCache.getBean(StatsService.class).listCacheStats(cacheName);
    }

    @Override
    public List<CacheStatsInfo> listCacheStats() {
        return BeanCache.getBean(StatsService.class).listCacheStats();
    }

    @Override
    public void resetCacheStat() {
        BeanCache.getBean(StatsService.class).resetCacheStat();
    }

    public boolean getStats() {
        return stats;
    }

    @Override
    public void destroy() throws Exception {
        BeanCache.getBean(StatsService.class).shutdownExecutor();
    }

    public void setStats(boolean stats) {
        this.stats = stats;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
