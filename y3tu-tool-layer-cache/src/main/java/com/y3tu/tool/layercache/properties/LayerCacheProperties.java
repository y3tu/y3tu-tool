package com.y3tu.tool.layercache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yuhao.wang3
 */
@Data
@ConfigurationProperties("spring.y3tu.layer-cache")
public class LayerCacheProperties {

    /**
     * 是否开启缓存统计
     */
    private boolean stats = true;

    /**
     * 命名空间，必须唯一般使用服务名
     */
    private String namespace;

    /**
     * 启动 LayerCacheViewServlet.
     */
    private boolean servletEnable = true;

    /**
     * contextPath
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

    /**
     * 登录用户账号
     */
    private String loginUsername = "admin";

    /**
     * 登录用户密码
     */
    private String loginPassword = "admin";

    /**
     * 是否启用更新权限
     */
    private boolean enableUpdate = true;

}
