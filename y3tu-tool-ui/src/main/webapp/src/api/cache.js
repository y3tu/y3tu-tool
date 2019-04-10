import request from '@/plugin/axios'

/**
 * 获取缓存统计列表
 */
export const getCacheList = (cacheName) => {
    let params = {
        cacheName
    };

    return request({
        url: 'cache-stats/list.json',
        method: 'post',
        params: params
    })
};

export const getCacheName = () => {
    return request({
        url: 'cache-stats/cache-name.json',
        method: 'get'
    });
};