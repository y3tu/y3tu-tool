package com.y3tu.tool.cache.core.manager;

import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.LayerCache;
import com.y3tu.tool.cache.core.cache.caffeine.CaffeineCache;
import com.y3tu.tool.cache.core.setting.LayerCacheSetting;

/**
 * @author y3tu
 * @date 2019-04-17
 */
public class FirstCacheManager extends AbstractCacheManager {
    @Override
    protected Cache getMissingCache(String name, LayerCacheSetting layerCacheSetting) {
        // 创建一级缓存
        CaffeineCache caffeineCache = new CaffeineCache(name, layerCacheSetting.getFirstCacheSetting(), getStats());
        return new LayerCache(null, caffeineCache, null, super.getStats(), layerCacheSetting);
    }
}
