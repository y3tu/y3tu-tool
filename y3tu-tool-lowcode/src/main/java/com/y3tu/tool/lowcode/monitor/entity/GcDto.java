package com.y3tu.tool.lowcode.monitor.entity;

import lombok.Data;

/**
 * 垃圾回收信息，堆内存信息
 *
 * @author y3tu
 */
@Data
public class GcDto {
    /**
     * Survivor0空间的大小。单位KB。
     */
    private String s0c;
    /**
     * Survivor1空间的大小。单位KB。
     */
    private String s1c;
    /**
     * Survivor0已用空间的大小。单位KB
     */
    private String s0u;
    /**
     * Survivor1已用空间的大小。单位KB。
     */
    private String s1u;
    /**
     * Eden空间的大小。单位KB。
     */
    private String ec;
    /**
     * Eden已用空间的大小。单位KB。
     */
    private String eu;
    /**
     * 老年代空间的大小。单位KB。
     */
    private String oc;
    /**
     * 老年代已用空间的大小。单位KB。
     */
    private String ou;
    /**
     * 元空间的大小（Metaspace）
     */
    private String mc;
    /**
     * 元空间已使用大小（KB）
     */
    private String mu;
    /**
     * 压缩类空间大小（compressed class space）
     */
    private String ccsc;
    /**
     * 压缩类空间已使用大小（KB）
     */
    private String ccsu;
    /**
     * 新生代gc次数
     */
    private String ygc;
    /**
     * 新生代gc耗时（秒）
     */
    private String ygct;
    /**
     * Full gc次数
     */
    private String fgc;
    /**
     * Full gc耗时（秒）
     */
    private String fgct;
    /**
     * gc总耗时（秒）
     */
    private String gct;
}
