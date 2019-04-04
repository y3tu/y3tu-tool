package com.y3tu.tool.cache.core.setting;


import com.y3tu.tool.cache.enums.ExpireMode;
import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 一级缓存配置项
 *
 * @author yuhao.wang
 */
@Data
public class FirstCacheSetting implements Serializable {

    /**
     * 缓存初始Size
     */
    private int initialCapacity;

    /**
     * 缓存最大Size
     */
    private int maximumSize;

    /**
     * 缓存有效时间
     */
    private int expireTime;

    /**
     * 缓存时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 缓存失效模式{@link ExpireMode}
     */
    private ExpireMode expireMode;

    /**
     * 是否允许存NULL值
     */
    private boolean allowNullValues;

    public FirstCacheSetting() {
    }

    /**
     * @param initialCapacity 缓存初始Size
     * @param maximumSize     缓存最大Size
     * @param expireTime      缓存有效时间
     * @param timeUnit        缓存时间单位 {@link TimeUnit}
     * @param expireMode      缓存失效模式{@link ExpireMode}
     */
    public FirstCacheSetting(int initialCapacity, int maximumSize, int expireTime, TimeUnit timeUnit, ExpireMode expireMode) {
        this.initialCapacity = initialCapacity;
        this.maximumSize = maximumSize;
        this.expireTime = expireTime;
        this.timeUnit = timeUnit;
        this.expireMode = expireMode;
        this.allowNullValues = true;
    }

}
