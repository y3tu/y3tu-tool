package com.y3tu.tool.cache.common;

import com.y3tu.tool.cache.common.impl.WeakCache;

/**
 * 缓存工具类
 *
 * @author Looly
 */
public class CacheUtil {

    /**
     * 创建FIFO(first in first out) 先进先出缓存.
     *
     * @param <K>      Key类型
     * @param <V>      Value类型
     * @param capacity 容量
     * @param timeout  过期时长，单位：毫秒
     * @return {@link com.y3tu.tool.cache.common.impl.FIFOCache}
     */
    public static <K, V> com.y3tu.tool.cache.common.impl.FIFOCache<K, V> newFIFOCache(int capacity, long timeout) {
        return new com.y3tu.tool.cache.common.impl.FIFOCache<K, V>(capacity, timeout);
    }

    /**
     * 创建FIFO(first in first out) 先进先出缓存.
     *
     * @param <K>      Key类型
     * @param <V>      Value类型
     * @param capacity 容量
     * @return {@link com.y3tu.tool.cache.common.impl.FIFOCache}
     */
    public static <K, V> com.y3tu.tool.cache.common.impl.FIFOCache<K, V> newFIFOCache(int capacity) {
        return new com.y3tu.tool.cache.common.impl.FIFOCache<K, V>(capacity);
    }

    /**
     * 创建LFU(least frequently used) 最少使用率缓存.
     *
     * @param <K>      Key类型
     * @param <V>      Value类型
     * @param capacity 容量
     * @param timeout  过期时长，单位：毫秒
     * @return {@link com.y3tu.tool.cache.common.impl.LFUCache}
     */
    public static <K, V> com.y3tu.tool.cache.common.impl.LFUCache<K, V> newLFUCache(int capacity, long timeout) {
        return new com.y3tu.tool.cache.common.impl.LFUCache<K, V>(capacity, timeout);
    }

    /**
     * 创建LFU(least frequently used) 最少使用率缓存.
     *
     * @param <K>      Key类型
     * @param <V>      Value类型
     * @param capacity 容量
     * @return {@link com.y3tu.tool.cache.common.impl.LFUCache}
     */
    public static <K, V> com.y3tu.tool.cache.common.impl.LFUCache<K, V> newLFUCache(int capacity) {
        return new com.y3tu.tool.cache.common.impl.LFUCache<K, V>(capacity);
    }


    /**
     * 创建LRU (least recently used)最近最久未使用缓存.
     *
     * @param <K>      Key类型
     * @param <V>      Value类型
     * @param capacity 容量
     * @param timeout  过期时长，单位：毫秒
     * @return {@link com.y3tu.tool.cache.common.impl.LRUCache}
     */
    public static <K, V> com.y3tu.tool.cache.common.impl.LRUCache<K, V> newLRUCache(int capacity, long timeout) {
        return new com.y3tu.tool.cache.common.impl.LRUCache<K, V>(capacity, timeout);
    }

    /**
     * 创建LRU (least recently used)最近最久未使用缓存.
     *
     * @param <K>      Key类型
     * @param <V>      Value类型
     * @param capacity 容量
     * @return {@link com.y3tu.tool.cache.common.impl.LRUCache}
     */
    public static <K, V> com.y3tu.tool.cache.common.impl.LRUCache<K, V> newLRUCache(int capacity) {
        return new com.y3tu.tool.cache.common.impl.LRUCache<K, V>(capacity);
    }

    /**
     * 创建定时缓存.
     *
     * @param <K>     Key类型
     * @param <V>     Value类型
     * @param timeout 过期时长，单位：毫秒
     * @return {@link com.y3tu.tool.cache.common.impl.TimedCache}
     */
    public static <K, V> com.y3tu.tool.cache.common.impl.TimedCache<K, V> newTimedCache(long timeout) {
        return new com.y3tu.tool.cache.common.impl.TimedCache<K, V>(timeout);
    }

    /**
     * 创建若引用缓存.
     *
     * @param <K>     Key类型
     * @param <V>     Value类型
     * @param timeout 过期时长，单位：毫秒
     * @return {@link com.y3tu.tool.cache.common.impl.WeakCache}
     */
    public static <K, V> com.y3tu.tool.cache.common.impl.WeakCache<K, V> newWeakCache(long timeout) {
        return new WeakCache<K, V>(timeout);
    }

    /**
     * 创建无缓存实现.
     *
     * @param <K> Key类型
     * @param <V> Value类型
     * @return {@link com.y3tu.tool.cache.common.impl.NoCache}
     */
    public static <K, V> com.y3tu.tool.cache.common.impl.NoCache<K, V> newNoCache() {
        return new com.y3tu.tool.cache.common.impl.NoCache<K, V>();
    }

}
