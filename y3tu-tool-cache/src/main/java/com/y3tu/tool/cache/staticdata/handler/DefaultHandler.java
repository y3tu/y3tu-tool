package com.y3tu.tool.cache.staticdata.handler;

import com.y3tu.tool.cache.core.cache.Cache;

/**
 * 默认处理
 *
 * @author y3tu
 */
public class DefaultHandler implements StaticDataHandler {

    @Override
    public void handler(String cacheName, Cache cache, Object cacheData) {
        //直接把方法执行后的结果放入缓存中,主键为[staticData_+缓存名]
        cache.putIfAbsent("staticData_" + cacheName, cacheData);
    }
}
