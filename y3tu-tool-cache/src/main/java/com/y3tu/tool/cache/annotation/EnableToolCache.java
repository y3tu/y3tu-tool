package com.y3tu.tool.cache.annotation;

import com.y3tu.tool.cache.config.FirstCacheConfig;
import com.y3tu.tool.cache.config.LayerCacheConfig;
import com.y3tu.tool.cache.config.LayerCacheProperties;
import com.y3tu.tool.cache.config.LayerCacheServletConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开始多级缓存配置
 *
 * @author y3tu
 * @date 2019-04-04
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableToolRedis
@Import({LayerCacheConfig.class, FirstCacheConfig.class,LayerCacheServletConfig.class})
@EnableConfigurationProperties(LayerCacheProperties.class)
public @interface EnableToolCache {

}
