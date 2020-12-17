import request from '@/plugin/axios'

/**
 * 获取缓存统计列表
 * @param data
 */
export function listCacheStats() {
    return request.get('y3tu-tool-cache/listCacheStats');
}

/**
 * 重置所有缓存统计
 */
export function resetAllCacheStat() {
    return request.get('y3tu-tool-cache/resetCacheStat');
}

/**
 * 根据缓存名称重置缓存统计
 */
export function resetCacheStatByCacheName(cacheName) {
    return request.get(`y3tu-tool-cache/resetCacheStat/${cacheName}`,);
}

/**
 * 清除所有缓存统计
 */
export function clearAllCache() {
    return request.get('y3tu-tool-cache/clearCache');
}

/**
 * 根据缓存名称清除缓存
 */
export function clearCacheByName(cacheName) {
    return request.get(`y3tu-tool-cache/clearCache/${cacheName}`,);
}

