package com.y3tu.tool.cache.layering.config;

import com.y3tu.tool.cache.layering.manager.CacheManager;
import com.y3tu.tool.cache.layering.manager.LayeringCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Import({RedisConfig.class})
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        LayeringCacheManager layeringCacheManager = new LayeringCacheManager(redisTemplate);
        // 开启统计功能
        layeringCacheManager.setStats(true);
        return layeringCacheManager;
    }

}
