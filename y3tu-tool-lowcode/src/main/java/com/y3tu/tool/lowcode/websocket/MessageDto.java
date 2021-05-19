package com.y3tu.tool.lowcode.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息
 *
 * @author y3tu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    /**
     * 心跳消息
     */
    public static final String TYPE_HEARTBEAT = "heartbeat";
    /**
     * 普通消息
     */
    public static final String TYPE_COMMON = "common";

    /**
     * 消息类型
     */
    String type;

    /**
     * 消息内容
     */
    String message;
}
