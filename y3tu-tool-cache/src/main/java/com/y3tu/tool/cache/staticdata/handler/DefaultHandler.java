package com.y3tu.tool.cache.staticdata.handler;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.LayeringCache;
import com.y3tu.tool.cache.core.cache.caffeine.CaffeineCache;
import com.y3tu.tool.cache.core.cache.redis.RedisCache;

/**
 * 默认缓存处理 缓存名和key相同
 *
 * @author y3tu
 */
public class DefaultHandler implements StaticDataHandler {

    @Override
    public void handler(String key, Cache cache, Object cacheData) {
        //直接把方法执行后的结果放入缓存中
        if (cache instanceof LayeringCache) {
            //多级缓存
            LayeringCache layeringCache = (LayeringCache) cache;
            layeringCache.getFirstCache().putIfAbsent(key, cacheData);
            layeringCache.getSecondCache().putIfAbsent(key, cacheData);
        } else if (cache instanceof RedisCache) {
            //redis缓存
            RedisCache redisCache = (RedisCache) cache;
            redisCache.putIfAbsent(key, cacheData);
        } else if (cache instanceof CaffeineCache) {
            //本地缓存
            CaffeineCache caffeineCache = (CaffeineCache) cache;
            caffeineCache.putIfAbsent(key, cacheData);
        }
    }
}
