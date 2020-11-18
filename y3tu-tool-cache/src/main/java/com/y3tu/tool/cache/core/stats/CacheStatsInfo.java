package com.y3tu.tool.cache.core.stats;

import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 缓存命中率统计实体类
 *
 * @author yuhao.wang3
 */
@Data
public class CacheStatsInfo implements Serializable {

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 内部缓存名，由[一级缓存有效时间-二级缓存有效时间-二级缓存自动刷新时间]组成
     */
    private String internalKey;

    /**
     * 描述,数据监控页面使用
     */
    private String depict;

    /**
     * 总请求总数
     */
    private long requestCount;

    /**
     * 总未命中总数
     */
    private long missCount;

    /**
     * 命中率
     */
    private double hitRate;

    /**
     * 一级缓存命中总数
     */
    private long firstCacheRequestCount;

    /**
     * 一级缓存未命中总数
     */
    private long firstCacheMissCount;

    /**
     * 二级缓存命中总数
     */
    private long secondCacheRequestCount;

    /**
     * 二级缓存未命中总数
     */
    private long secondCacheMissCount;

    /**
     * 总的请求时间
     */
    private long totalLoadTime;

    /**
     * 缓存配置
     */
    private LayeringCacheSetting layeringCacheSetting;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CacheStatsInfo that = (CacheStatsInfo) o;

        if (!Objects.equals(cacheName, that.cacheName)) {
            return false;
        }
        return Objects.equals(internalKey, that.internalKey);
    }

    @Override
    public int hashCode() {
        int result = cacheName != null ? cacheName.hashCode() : 0;
        result = 31 * result + (internalKey != null ? internalKey.hashCode() : 0);
        return result;
    }

    /**
     * 清空统计信息
     */
    public void clearStatsInfo() {
        this.setRequestCount(0);
        this.setMissCount(0);
        this.setTotalLoadTime(0);
        this.setHitRate(0);

        this.setFirstCacheRequestCount(0);
        this.setFirstCacheMissCount(0);

        this.setSecondCacheRequestCount(0);
        this.setSecondCacheMissCount(0);
    }
}
