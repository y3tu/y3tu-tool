package com.y3tu.tool.cache.layering.listener;

import com.y3tu.tool.cache.layering.manager.AbstractCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * redis消息的监听
 *
 * @author yuhao.wang
 */
@Slf4j
public class RedisMessageListener extends MessageListenerAdapter {
    /**
     * 缓存管理器
     */
    private AbstractCacheManager cacheManager;
}
