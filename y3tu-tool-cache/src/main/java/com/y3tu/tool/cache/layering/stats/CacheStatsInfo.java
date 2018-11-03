package com.y3tu.tool.cache.layering.stats;


import com.y3tu.tool.cache.layering.setting.LayeringCacheSetting;
import lombok.Data;

/**
 * 缓存命中率统计实体类
 *
 * @author yuhao.wang3
 */
@Data
public class CacheStatsInfo {

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

        if (cacheName != null ? !cacheName.equals(that.cacheName) : that.cacheName != null) {
            return false;
        }
        return internalKey != null ? internalKey.equals(that.internalKey) : that.internalKey == null;
    }

    @Override
    public int hashCode() {
        int result = cacheName != null ? cacheName.hashCode() : 0;
        result = 31 * result + (internalKey != null ? internalKey.hashCode() : 0);
        return result;
    }
}
