import request from '@/plugin/axios'

/**
 * 获取缓存统计列表
 * @param data
 */
export function listCacheStats() {
    return request.get('y3tu-tool-cache/listCacheStats');
}


