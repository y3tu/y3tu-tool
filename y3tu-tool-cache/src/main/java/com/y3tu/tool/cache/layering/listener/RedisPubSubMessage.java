package com.y3tu.tool.cache.layering.listener;

import lombok.Data;

/**
 * redis pub/sub 消息
 *
 * @author yuhao.wang3
 */
@Data
public class RedisPubSubMessage {
    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 缓存key
     */
    private Object key;

    /**
     * 消息类型
     */
    private RedisPubSubMessageType messageType;

}