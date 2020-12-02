package com.y3tu.tool.cache.rest;

import com.y3tu.tool.cache.core.manager.CacheManager;
import com.y3tu.tool.cache.core.stats.CacheStatsInfo;
import com.y3tu.tool.core.pojo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 缓存请求接口
 *
 * @author y3tu
 */
@RestController
@RequestMapping("/tool/cache")
public class ToolCacheController {

    @Autowired
    CacheManager cacheManager;

    /**
     * 获取所有缓存统计列表
     *
     * @return
     */
    @GetMapping("cacheStatsList")
    public R allCacheStatsList() {
        List<CacheStatsInfo> cacheStatsInfoList = new ArrayList<>();
        Collection<String> cacheNames = cacheManager.getCacheNames();
        for (String cacheName : cacheNames) {
            List<CacheStatsInfo> cacheStatsInfos = cacheManager.listCacheStats(cacheName);
            if (!cacheStatsInfos.isEmpty()) {
                cacheStatsInfoList.addAll(cacheStatsInfos);
            }
        }
        return R.success(cacheStatsInfoList);
    }

    /**
     * 获取指定缓存统计列表
     */
    @GetMapping("cacheStatsList/{cacheName}")
    public R cacheStatsList(@PathVariable String cacheName) {
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

}
