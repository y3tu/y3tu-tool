package com.y3tu.tool.cache.service;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.manager.CacheManager;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import com.y3tu.tool.core.util.BeanCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Collection;


/**
 * 操作缓存的服务
 *
 * @author y3tu
 */
@Service
public class ToolCacheService {

    @Autowired
    CacheManager cacheManager;

    /**
     * 根据静态数据名和配置获取数据
     *
     * @param cacheName            缓存名
     * @param layeringCacheSetting 缓存配置
     * @return
     */
    public Object loadStaticData(String cacheName, LayeringCacheSetting layeringCacheSetting, Class clazz, Method method) {
        Object object = BeanCacheUtil.getBean(clazz);
        Cache cache = cacheManager.getCache(cacheName, layeringCacheSetting);
        // 通Cache获取值
        return cache.get(layeringCacheSetting.getInternalKey(), () -> method.invoke(object));
    }

    /**
     * 清除静态数据
     *
     * @param cacheName 缓存名
     */
    public void clearStaticData(String cacheName) {
        Collection<Cache> caches = cacheManager.getCache(cacheName);
        for (Cache cache : caches) {
            cache.clear();
        }
    }


}
