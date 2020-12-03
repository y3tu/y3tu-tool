package com.y3tu.tool.cache.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 允许访问接口
 *
 * @author y3tu
 */
@Configuration
@ConditionalOnProperty(prefix = "y3tu.tool.cache", name = "enableApi", havingValue = "true")
@ComponentScan("com.y3tu.tool.cache.rest")
public class ToolCacheApiConfigure {
}
