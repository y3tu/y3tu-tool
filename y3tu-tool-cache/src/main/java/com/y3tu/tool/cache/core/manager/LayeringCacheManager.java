package com.y3tu.tool.cache.core.manager;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.LayeringCache;
import com.y3tu.tool.cache.core.cache.caffeine.CaffeineCache;
import com.y3tu.tool.cache.core.cache.redis.RedisCache;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 多级缓存管理
 *
 * @author yuhao.wang
 */
@Slf4j
public class LayeringCacheManager extends SecondaryCacheManager {

    public LayeringCacheManager(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    /**
     * 根据缓存名称在CacheManager中没有找到对应Cache时，通过该方法新建一个对应的Cache实例
     *
     * @param name                 缓存名称
     * @param layeringCacheSetting 缓存配置
     * @return {@link Cache}
     */
    @Override
    public Cache getMissingCache(String name, LayeringCacheSetting layeringCacheSetting) {
        // 创建一级缓存
        CaffeineCache caffeineCache = new CaffeineCache(name, isStats(), layeringCacheSetting);
        // 创建二级缓存
        RedisCache redisCache = new RedisCache(name, isStats(), redisTemplate, layeringCacheSetting);
        return new LayeringCache(redisTemplate, caffeineCache, redisCache, isStats(), layeringCacheSetting);
    }

}
