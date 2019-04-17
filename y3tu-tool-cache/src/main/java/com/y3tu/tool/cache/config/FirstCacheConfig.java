package com.y3tu.tool.cache.config;

import com.y3tu.tool.cache.aspect.LayerCacheAspect;
import com.y3tu.tool.cache.core.manager.CacheManager;
import com.y3tu.tool.cache.core.manager.FirstCacheManager;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 如果仅使用一级缓存的配置
 *
 * @author y3tu
 * @date 2019-04-17
 */
@Configuration
@ConditionalOnClass(Aspect.class)
@EnableAspectJAutoProxy
public class FirstCacheConfig {

    @Bean
    public LayerCacheAspect layerCacheAspect() {
        return new LayerCacheAspect();
    }

    @Bean(name = "firstCacheManager")
    @ConditionalOnMissingBean(name = "redisCacheManager")
    public CacheManager cacheManager(@Qualifier("layerCacheProperties") LayerCacheProperties properties) {
        FirstCacheManager firstCacheManager = new FirstCacheManager();
        // 默认关闭统计功能
        firstCacheManager.setStats(properties.isStats());
        return firstCacheManager;
    }
}
