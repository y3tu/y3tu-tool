package com.y3tu.tool.layercache.config;

import com.y3tu.tool.layercache.aspectj.aspect.LayeringAspect;
import com.y3tu.tool.layercache.core.manager.CacheManager;
import com.y3tu.tool.layercache.core.manager.LayerCacheManager;
import com.y3tu.tool.layercache.properties.LayerCacheProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 多级缓存自动配置类
 *
 * @author xiaolyuh
 */
@Configuration
@ConditionalOnBean(RedisTemplate.class)
@AutoConfigureAfter({RedisAutoConfiguration.class})
@EnableAspectJAutoProxy
@EnableConfigurationProperties({LayerCacheProperties.class})
@Import({LayerCacheServletConfig.class})
public class LayerCacheAutoConfig {

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager cacheManager(RedisTemplate redisTemplate, LayerCacheProperties properties) {
        LayerCacheManager layeringCacheManager = new LayerCacheManager(redisTemplate);
        // 默认开启统计功能
        layeringCacheManager.setStats(properties.isStats());
        return layeringCacheManager;
    }

    @Bean
    public LayeringAspect layeringAspect() {
        return new LayeringAspect();
    }

}
