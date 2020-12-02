package com.y3tu.tool.cache.service;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.manager.CacheManager;
import com.y3tu.tool.cache.core.setting.FirstCacheSetting;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import com.y3tu.tool.cache.core.setting.SecondaryCacheSetting;
import com.y3tu.tool.cache.core.stats.SecondaryStatsService;
import com.y3tu.tool.core.util.BeanCacheUtil;
import com.y3tu.tool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * 操作缓存的服务
 *
 * @author y3tu
 */
@Service
public class ToolCacheService {

    @Autowired
    CacheManager cacheManager;

    /**
     * 删除缓存
     *
     * @param cacheName   缓存名称
     * @param internalKey 内部缓存名，由[一级缓存有效时间-二级缓存有效时间-二级缓存自动刷新时间]组成
     * @param key         key，可以为NULL，如果是NULL则清空缓存
     */
    public void deleteCache(String cacheName, String internalKey, String key) {
        if (StrUtil.isBlank(cacheName) || StrUtil.isBlank(internalKey)) {
            return;
        }
        LayeringCacheSetting defaultSetting = new LayeringCacheSetting(new FirstCacheSetting(), new SecondaryCacheSetting(), "默认缓存配置（删除时生成）");

        if (StrUtil.isBlank(key)) {
            // 清空缓存
            // 删除缓存统计信息
            String redisKey = SecondaryStatsService.CACHE_STATS_KEY_PREFIX + cacheName + internalKey;
            BeanCacheUtil.getBean(SecondaryStatsService.class).resetCacheStat(redisKey);

            // 删除缓存
            Collection<Cache> caches = cacheManager.getCache(cacheName);
            if (CollectionUtils.isEmpty(caches)) {
                // 如果没有找到Cache就新建一个默认的
                Cache cache = cacheManager.getCache(cacheName, defaultSetting);
                cache.clear();

                // 删除统计信息
                redisKey = SecondaryStatsService.CACHE_STATS_KEY_PREFIX + cacheName + defaultSetting.getInternalKey();
                BeanCacheUtil.getBean(SecondaryStatsService.class).resetCacheStat(redisKey);
            } else {
                for (Cache cache : caches) {
                    cache.clear();
                }
            }
        }

        Collection<Cache> caches = cacheManager.getCache(cacheName);
        if (CollectionUtils.isEmpty(caches)) {
            // 如果没有找到Cache就新建一个默认的
            Cache cache = cacheManager.getCache(cacheName, defaultSetting);
            cache.evict(key);
        } else {
            for (Cache cache : caches) {
                cache.evict(key);
            }
        }
    }
}
