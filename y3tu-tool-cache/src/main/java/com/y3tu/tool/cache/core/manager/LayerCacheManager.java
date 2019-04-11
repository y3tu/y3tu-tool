package com.y3tu.tool.cache.core.manager;


import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.LayerCache;
import com.y3tu.tool.cache.core.cache.caffeine.CaffeineCache;
import com.y3tu.tool.cache.core.cache.redis.RedisCache;
import com.y3tu.tool.cache.core.setting.LayerCacheSetting;
import com.y3tu.tool.cache.enums.CacheMode;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author yuhao.wang
 */
public class LayerCacheManager extends AbstractCacheManager {

    public LayerCacheManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        cacheManagers.add(this);
    }

    @Override
    protected Cache getMissingCache(String name, LayerCacheSetting layerCacheSetting) {
        CacheMode cacheMode = layerCacheSetting.getCacheMode();
        if (CacheMode.ALL.equals(cacheMode)) {
            // 创建一级缓存
            CaffeineCache caffeineCache = new CaffeineCache(name, layerCacheSetting.getFirstCacheSetting(), getStats());
            // 创建二级缓存
            RedisCache redisCache = new RedisCache(name, redisTemplate, layerCacheSetting.getSecondaryCacheSetting(), getStats());
            return new LayerCache(redisTemplate, caffeineCache, redisCache, super.getStats(), layerCacheSetting);
        } else if (CacheMode.ONLY_FIRST.equals(cacheMode)) {
            //只是用一级缓存
            // 创建一级缓存
            CaffeineCache caffeineCache = new CaffeineCache(name, layerCacheSetting.getFirstCacheSetting(), getStats());
            return caffeineCache;
        } else if (CacheMode.ONLY_SECOND.equals(cacheMode)) {
            //只是使用二级缓存
            // 创建二级缓存
            RedisCache redisCache = new RedisCache(name, redisTemplate, layerCacheSetting.getSecondaryCacheSetting(), getStats());
            return redisCache;
        }
        return null;
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
