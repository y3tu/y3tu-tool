package com.y3tu.tool.cache.annotation;

import com.y3tu.tool.cache.config.LayerCacheAutoConfig;
import com.y3tu.tool.cache.config.LayerCacheProperties;
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
@Import({LayerCacheAutoConfig.class, LayerCacheProperties.class})
public @interface EnableToolLayerCache {
}
