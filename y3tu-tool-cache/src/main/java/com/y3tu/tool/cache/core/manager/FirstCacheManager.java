package com.y3tu.tool.cache.core.manager;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.caffeine.CaffeineCache;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import com.y3tu.tool.cache.core.stats.CacheStatsInfo;
import com.y3tu.tool.cache.core.stats.FirstStatsService;
import com.y3tu.tool.core.util.BeanCacheUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 一级缓存管理
 *
 * @author y3tu
 */
@Slf4j
public class FirstCacheManager extends AbstractCacheManager {


    @Override
    protected Cache getMissingCache(String name, LayeringCacheSetting layeringCacheSetting) {
        // 创建一级缓存
        CaffeineCache caffeineCache = new CaffeineCache(name, isStats(), layeringCacheSetting);
        return caffeineCache;
    }


    @Override
    public List<CacheStatsInfo> listCacheStats(String cacheName) {
        return BeanCacheUtil.getBean(FirstStatsService.class).listCacheStats(cacheName);
    }

    @Override
    public void resetCacheStat() {
        BeanCacheUtil.getBean(FirstStatsService.class).resetCacheStat();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void afterPropertiesSet() {

    }
}
