package com.y3tu.tool.cache.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 多级缓存配置
 * @author yuhao.wang3
 */
@Data
@ConfigurationProperties("y3tu.tool.cache.layer")
@Qualifier("layerCacheProperties")
public class LayerCacheProperties {

    /**
     * 是否关闭缓存统计
     */
    private boolean stats = false;

    /**
     * 命名空间，必须唯一般使用服务名
     */
    private String namespace;

    /**
     * 启动 LayerCacheViewServlet.
     */
    private boolean servletEnable = true;

    /**
     * 设置拦截的url
     */
    private String urlPattern;

    /**
     * 白名单
     */
    private String allow;

    /**
     * 黑名单
     */
    private String deny;


}
