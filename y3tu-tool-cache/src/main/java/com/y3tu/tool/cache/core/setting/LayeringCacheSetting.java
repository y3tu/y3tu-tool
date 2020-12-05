package com.y3tu.tool.cache.core.setting;

import lombok.Data;

import java.io.Serializable;

/**
 * 多级缓存配置项
 *
 * @author yuhao.wang
 */
@Data
public class LayeringCacheSetting implements Serializable {

    private static final String SPLIT = "-";

    /**
     * 描述，数据监控页面使用
     */
    private String depict;

    /**
     * 是否使用一级缓存
     */
    boolean useFirstCache = true;

    /**
     * 一级缓存配置
     */
    private FirstCacheSetting firstCacheSetting;

    /**
     * 二级缓存配置
     */
    private SecondaryCacheSetting secondaryCacheSetting;

    public LayeringCacheSetting() {
    }

    public LayeringCacheSetting(FirstCacheSetting firstCacheSetting, SecondaryCacheSetting secondaryCacheSetting,
                                String depict) {
        this.firstCacheSetting = firstCacheSetting;
        this.secondaryCacheSetting = secondaryCacheSetting;
        this.depict = depict;
    }

}
