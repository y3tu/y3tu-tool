package com.y3tu.tool.cache.rest;

import com.y3tu.tool.cache.core.manager.CacheManager;
import com.y3tu.tool.cache.core.stats.CacheStatsInfo;
import com.y3tu.tool.core.pojo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 缓存请求接口
 *
 * @author y3tu
 */
@RestController
@RequestMapping("${y3tu.tool.cache.urlPattern:y3tu-tool-cache}")
public class ToolCacheController {

    @Autowired
    CacheManager cacheManager;

    /**
     * 获取所有缓存统计列表
     */
    @GetMapping("listCacheStats")
    public R listCacheStats() {
        List<CacheStatsInfo> cacheStatsInfoList = cacheManager.listCacheStats();
        return R.success(cacheStatsInfoList);
    }

    /**
     * 获取指定缓存统计列表
     */
    @GetMapping("listCacheStats/{cacheName}")
    public R listCacheStats(@PathVariable String cacheName) {
        List<CacheStatsInfo> cacheStatsInfoList = cacheManager.listCacheStats(cacheName);
        return R.success(cacheStatsInfoList);
    }

    /**
     * 重置缓存统计数据
     */
    @GetMapping("resetCacheStat")
    public R resetCacheStat() {
        cacheManager.resetCacheStat();
        return R.success();
    }

    /**
     * 根据缓存名重置缓存统计数据
     */
    @GetMapping("resetCacheStat/{cacheName}")
    public R resetCacheStat(@PathVariable String cacheName) {
        cacheManager.resetCacheStat(cacheName);
        return R.success();
    }

    /**
     * 清空缓存
     *
     * @return
     */
    @GetMapping("clearCache")
    public R clearCache() {
        cacheManager.clearCache();
        return R.success();
    }

    /**
     * 根据缓存名清空缓存
     */
    @GetMapping("clearCache/{cacheName}")
    public R clearCache(@PathVariable String cacheName) {
        cacheManager.clearCache(cacheName);
        return R.success();
    }
}
