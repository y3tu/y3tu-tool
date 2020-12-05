package com.y3tu.tool.cache.configure;

import com.y3tu.tool.cache.aspect.CacheAspect;
import com.y3tu.tool.cache.core.manager.*;
import com.y3tu.tool.cache.core.support.CacheMode;
import com.y3tu.tool.cache.properties.CacheProperties;
import com.y3tu.tool.cache.runner.ToolCacheStartedRunner;
import com.y3tu.tool.cache.service.ToolCacheService;
import com.y3tu.tool.cache.staticdata.StaticDataService;
import com.y3tu.tool.core.exception.ToolException;
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
@Import({ToolRedisAutoConfigure.class, ToolCacheStartedRunner.class, ToolCacheApiConfigure.class, ToolCacheService.class, StaticDataService.class})
public class ToolCacheAutoConfigure {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    @Order(2)
    public CacheManager layeringCacheManager(CacheProperties properties) {

        AbstractCacheManager cacheManager = null;
        try {
            if (properties.getCacheMode() == CacheMode.ONLY_FIRST) {
                //只开启一级缓存
                cacheManager = new FirstCacheManager(properties.isStats());
            } else if (properties.getCacheMode() == CacheMode.ONLY_SECOND) {
                //只开启二级缓存
                RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("ToolCacheRedisTemplate", RedisTemplate.class);
                cacheManager = new SecondaryCacheManager(redisTemplate, properties.isStats());
            } else {
                //开启多级缓存
                RedisTemplate<String, Object> redisTemplate = applicationContext.getBean("ToolCacheRedisTemplate", RedisTemplate.class);
                cacheManager = new LayeringCacheManager(redisTemplate, properties.isStats());
            }
        } catch (NoClassDefFoundError e) {
            throw new ToolException("二级或多级缓存需要添加Redis依赖！");
        }

        return cacheManager;
    }

    /**
     * 开启缓存切面操作
     *
     * @return
     */
    @Bean
    public CacheAspect layeringAspect() {
        return new CacheAspect();
    }


}
