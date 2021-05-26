package com.y3tu.tool.lowcode.monitor.entity;

import lombok.Data;

/**
 * 类加载信息
 *
 * @author y3tu
 */
@Data
public class ClassLoadDto {
    /**
     * 表示载入了类的数量
     */
    private String loaded;
    /**
     * 表示载入了类的合计
     */
    private String bytes1;
    /**
     * 表示卸载类的数量
     */
    private String unloaded;
    /**
     * 表示卸载类合计大小
     */
    private String bytes2;
    /**
     * 表示加载和卸载类总共的耗时
     */
    private String time1;
    /**
     * 表示编译任务执行的次数
     */
    private String compiled;
    /**
     * 表示编译失败的次数
     */
    private String failed;
    /**
     * 表示编译不可用的次数
     */
    private String invalid;
    /**
     * 表示编译的总耗时
     */
    private String time2;
}
