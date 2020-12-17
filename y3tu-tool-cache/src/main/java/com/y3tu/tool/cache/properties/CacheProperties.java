package com.y3tu.tool.cache.properties;

import com.y3tu.tool.cache.core.support.CacheMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 缓存基本配置
 *
 * @author y3tu
 */
@Data
@ConfigurationProperties("y3tu.tool.cache")
public class CacheProperties {

    /**
     * 是否开启缓存统计 默认关闭
     */
    private boolean stats = false;
    /**
     * 启动请求接口 默认关闭
     */
    private boolean enableApi = false;
    /**
     * 请求接口访问前缀
     * 默认y3tu-tool-cache
     */
    private String urlPattern = "y3tu-tool-cache";
    /**
     * 缓存模式
     */
    private CacheMode cacheMode = CacheMode.ONLY_FIRST;

    /**
     * 静态数据处理包
     */
    private String[] staticDataPackage;

}
