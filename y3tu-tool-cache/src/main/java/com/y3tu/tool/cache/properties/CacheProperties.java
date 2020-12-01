package com.y3tu.tool.cache.properties;

import com.y3tu.tool.cache.core.support.CacheMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 缓存基本配置
 *
 * @author yuhao.wang3
 */
@Data
@ConfigurationProperties("y3tu.tool.cache")
public class CacheProperties {

    /**
     * 是否开启缓存统计
     */
    private boolean stats = false;

    /**
     * 命名空间，必须唯一般使用服务名
     */
    private String namespace;

    /**
     * 启动 CacheServlet.
     */
    private boolean servletEnabled = true;

    /**
     * contextPath
     */
    private String urlPattern;
    /**
     * 缓存模式
     */
    private CacheMode cacheMode = CacheMode.ONLY_FIRST;


}
