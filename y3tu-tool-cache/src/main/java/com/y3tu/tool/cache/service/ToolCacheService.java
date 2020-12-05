package com.y3tu.tool.cache.service;

import com.y3tu.tool.cache.core.manager.CacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 操作缓存的服务
 *
 * @author y3tu
 */
@Service
@Slf4j
public class ToolCacheService {

    @Autowired
    CacheManager cacheManager;

    /**
     * 清除缓存
     *
     * @param cacheName 缓存名
     */
    public void clearCache(String cacheName) {
        cacheManager.clearCache(cacheName);
    }


}
