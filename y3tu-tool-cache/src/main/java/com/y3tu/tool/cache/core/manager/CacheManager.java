package com.y3tu.tool.cache.core.manager;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.setting.LayerCacheSetting;
import com.y3tu.tool.cache.core.stats.CacheStatsInfo;
import java.util.Collection;
import java.util.List;

/**
 * 缓存管理器
 * 允许通过缓存名称来获的对应的 {@link Cache}.
 *
 * @author yuhao.wang3
 */
public interface CacheManager {

    /**
     * 根据缓存名称返回对应的{@link Collection}.
     *
     * @param name 缓存的名称 (不能为 {@code null})
     * @return 返回对应名称的Cache, 如果没找到返回 {@code null}
     */
    Collection<Cache> getCache(String name);

    /**
     * 根据缓存名称返回对应的{@link Cache}，如果没有找到就新建一个并放到容器
     *
     * @param name                 缓存名称
     * @param layerCacheSetting 多级缓存配置
     * @return {@link Cache}
     */
    Cache getCache(String name, LayerCacheSetting layerCacheSetting);

    /**
     * 获取所有缓存名称的集合
     *
     * @return 所有缓存名称的集合
     */
    Collection<String> getCacheNames();

    /**
     * 获取缓存命中率统计信息
     *
     * @param cacheName 缓存名称，为Blank则查询全部
     * @return List&lt;CacheStatsInfo&gt;
     */
    List<CacheStatsInfo> listCacheStats(String cacheName);

    /**
     * 获取全部缓存数据
     * @return
     */
    List<CacheStatsInfo> listCacheStats();
    /**
     * 重置缓存统计数据
     */
    void resetCacheStat();
}
