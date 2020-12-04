package com.y3tu.tool.cache.service;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.manager.CacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     * 根据缓存名获取静态数据
     *
     * @param cacheName 缓存名
     * @return
     */
    public Object getStaticData(String cacheName) {
        return getCacheData(cacheName, "staticData_" + cacheName);
    }

    /**
     * 根据缓存名和key获取缓存数据
     *
     * @param cacheName 缓存名
     * @param key       主键
     * @return
     */
    public Object getCacheData(String cacheName, String key) {
        Collection<Cache> caches = cacheManager.getCache(cacheName);
        if (caches.size() > 1) {
            List<Object> list = new ArrayList<>();
            for (Cache cache : caches) {
                list.add(cache.get(key));
            }
            return list;
        } else if (caches.size() == 1) {
            for (Cache cache : caches) {
                return cache.get(key);
            }
        }
        return null;
    }


    /**
     * 清除缓存
     *
     * @param cacheName 缓存名
     */
    public void clearCache(String cacheName) {
        Collection<Cache> caches = cacheManager.getCache(cacheName);
        for (Cache cache : caches) {
            cache.clear();
        }
    }


}
