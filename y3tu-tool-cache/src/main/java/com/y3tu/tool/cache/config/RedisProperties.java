package com.y3tu.tool.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis配置
 */
@Data
@ConfigurationProperties("y3tu.tool.cache.redis")
public class RedisProperties {
    private Integer maxIdle;
    private Integer maxTotal;
    private Integer maxWaitMillis;
    private Integer minEvictableIdleTimeMillis;
    private Integer numTestsPerEvictionRun;
    private long timeBetweenEvictionRunsMillis;
    private boolean testOnBorrow;
    private boolean testWhileIdle;
    private String clusterNodes;
    private Integer maxRedirects;
    private String hostName;
    private Integer port;
    private String password;


}
