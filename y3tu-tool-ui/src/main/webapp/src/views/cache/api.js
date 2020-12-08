import request from '@/plugin/axios'

/**
 * 获取缓存统计列表
 * @param data
 */
export function getCacheStats() {
    return request.get('base/yao-cache/cache-stats/list');
}

/**
 * 清除缓存
 * @returns {*}
 */
export function deleteCache(data) {
    return request.get('base/yao-cache/cache-stats/delete-cache',data);
}

/**
 * 重置缓存统计
 * @returns {*}
 */
export function resetCacheStats() {
    return request.get('base/yao-cache/cache-stats/reset-stats');
}

