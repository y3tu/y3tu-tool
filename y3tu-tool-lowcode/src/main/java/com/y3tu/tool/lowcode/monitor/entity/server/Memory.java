package com.y3tu.tool.lowcode.monitor.entity.server;

import lombok.Data;

/**
 * 内存信息
 *
 * @author y3tu
 */
@Data
public class Memory {
    /**
     * 内存总量
     */
    private double total;

    /**
     * 已用内存
     */
    private double used;

    /**
     * 剩余内存
     */
    private double free;

}
