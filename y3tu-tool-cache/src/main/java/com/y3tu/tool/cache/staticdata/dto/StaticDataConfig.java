package com.y3tu.tool.cache.staticdata.dto;

import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import com.y3tu.tool.cache.staticdata.handler.StaticDataHandler;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * 静态数据配置
 *
 * @author y3tu
 */
@Data
public class StaticDataConfig {
    /**
     * 静态资料名称
     */
    private String name;

    /**
     * 缓存key
     */
    private String key;
    /**
     * 数据收集实现类
     */
    private Class clazz;

    /**
     * 数据收集实现方法
     */
    private Method method;

    /**
     * 缓存数据
     */
    private Object cacheData;

    /**
     * 是否程序启动后就加载
     */
    private boolean isStartUp;

    /**
     * 缓存配置
     */
    LayeringCacheSetting layeringCacheSetting;
    /**
     * 缓存数据特殊处理
     */
    Class<? extends StaticDataHandler> staticDataHandler;
}
