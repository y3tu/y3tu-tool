package com.y3tu.tool.cache.core.setting;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 二级缓存配置项
 *
 * @author yuhao.wang
 */
@Data
public class SecondaryCacheSetting implements Serializable {
    /**
     * 缓存有效时间
     */
    private long expiration;

    /**
     * 缓存主动在失效前强制刷新缓存的时间
     */
    private long preloadTime;

    /**
     * 时间单位 {@link TimeUnit}
     */
    private TimeUnit timeUnit;

    /**
     * 是否强制刷新（走数据库），默认是false
     */
    private boolean forceRefresh = false;

    /**
     * 是否使用缓存名称作为 redis key 前缀
     */
    private boolean usePrefix;

    /**
     * 是否允许存NULL值
     */
    private boolean allowNullValues;

    public SecondaryCacheSetting() {
    }

    /**
     * @param expiration   缓存有效时间
     * @param preloadTime  缓存刷新时间
     * @param timeUnit     时间单位 {@link TimeUnit}
     * @param forceRefresh 是否强制刷新
     */
    public SecondaryCacheSetting(long expiration, long preloadTime, TimeUnit timeUnit, boolean forceRefresh) {
        this.expiration = expiration;
        this.preloadTime = preloadTime;
        this.timeUnit = timeUnit;
        this.forceRefresh = forceRefresh;
        this.usePrefix = true;
        this.allowNullValues = true;
    }

}
