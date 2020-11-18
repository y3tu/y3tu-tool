import request from '@/plugin/axios'
import axios from 'axios'

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

/**
 * 删除指定缓存
 */
export const deleteCache = (params) => {
    return request({
        url: 'cache-stats/delete-cache.json',
        method: 'post',
        params: params
    });
};

/**
 * 重置缓存统计数据
 */
export const resetCacheStats=()=>{
    return request({
        url: 'cache-stats/reset-stats.json',
        method: 'get'
    });
};

export const testPut = () => {
    let person = {
        id: 1,
        name: "y3tu",
        age: 29,
        address: "重庆"
    };

    let request = axios.create({
        baseURL: "http://127.0.0.1:8080/",
        withCredentials: true, // 跨域请求，允许保存cookie
        timeout: 15000 // 请求超时时间
    });

    return request({
        url: '/cacheTest/put',
        method: 'get',
    })
};
