package com.y3tu.tool.cache.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yuhao.wang3
 */
@ConfigurationProperties("y3tu.tool.cache")
@Data
public class CacheProperties {

    /**
     * 是否开启缓存统计
     */
    private boolean stats = true;

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


}
