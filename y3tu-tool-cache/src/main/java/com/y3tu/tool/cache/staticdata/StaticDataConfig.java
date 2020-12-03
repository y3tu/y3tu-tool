package com.y3tu.tool.cache.staticdata;

import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * 静态数据配置
 */
@Data
public class StaticDataConfig {
    /**
     * 静态资料名称
     */
    private String name;

    /**
     * 数据收集实现类
     */
    private Class clazz;

    /**
     * 数据收集实现方法
     */
    private Method method;

    /**
     * 缓存配置
     */
    LayeringCacheSetting layeringCacheSetting;

}
