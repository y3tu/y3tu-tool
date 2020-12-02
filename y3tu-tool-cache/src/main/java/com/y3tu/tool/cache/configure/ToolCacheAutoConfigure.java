package com.y3tu.tool.cache.configure;

import com.y3tu.tool.cache.aspect.LayeringAspect;
import com.y3tu.tool.cache.core.manager.*;
import com.y3tu.tool.cache.core.support.CacheMode;
import com.y3tu.tool.cache.properties.CacheProperties;
import com.y3tu.tool.cache.runner.ToolCacheStartedRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 多级缓存自动配置类
 *
 * @author xiaolyuh
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties({CacheProperties.class})
@Import(ToolCacheStartedRunner.class)
public class ToolCacheAutoConfigure {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    @Order(2)
    public CacheManager layeringCacheManager(CacheProperties properties) {

        AbstractCacheManager cacheManager = null;
        if (properties.getCacheMode() == CacheMode.ONLY_FIRST) {
            //只开启一级缓存
            cacheManager = new FirstCacheManager();
        } else if (properties.getCacheMode() == CacheMode.ONLY_SECOND) {
            //只开启二级缓存
            RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("ToolCacheRedisTemplate", RedisTemplate.class);
            cacheManager = new SecondaryCacheManager(redisTemplate);
        } else {
            //开启多级缓存
            RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("ToolCacheRedisTemplate", RedisTemplate.class);
            cacheManager = new LayeringCacheManager(redisTemplate);
        }

        // 默认关闭统计功能
        cacheManager.setStats(properties.isStats());
        return cacheManager;
    }

    /**
     * 开启缓存切面操作
     *
     * @return
     */
    @Bean
    public LayeringAspect layeringAspect() {
        return new LayeringAspect();
    }

}
