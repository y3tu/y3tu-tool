package com.y3tu.tool.cache.core.cache;

import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.cache.core.stats.CacheStats;
import com.y3tu.tool.cache.core.support.NullValue;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;


/**
 * Cache 接口的抽象实现类，对公共的方法做了一写实现，如是否允许存NULL值
 * <p>如果允许为NULL值，则需要在内部将NULL替换成{@link NullValue#INSTANCE} 对象
 *
 * @author yuhao.wang3
 */
public abstract class AbstractValueAdaptingCache implements Cache {

    /**
     * 是否允许为NULL
     */
    private final boolean allowNullValues;

    /**
     * 缓存名称
     */
    private final String name;

    /**
     * 是否开启统计功能
     */
    private boolean stats;

    /**
     * 缓存统计类
     */
    private CacheStats cacheStats = new CacheStats();

    /**
     * 通过构造方法设置缓存配置
     *
     * @param allowNullValues 是否允许为NULL
     * @param stats           是否开启监控统计
     * @param name            缓存名称
     */
    protected AbstractValueAdaptingCache(boolean allowNullValues, boolean stats, String name) {
        Assert.notNull(name, "缓存名称不能为NULL");
        this.allowNullValues = allowNullValues;
        this.stats = stats;
        this.name = name;
    }

    /**
     * 获取是否允许存NULL值
     *
     * @return true:允许，false:不允许
     */
    public final boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        return (T) fromStoreValue(get(key));
    }

    /**
     * NullValue转换为null
     *
     * @param storeValue 需要转换的
     * @return 转换后的
     */
    protected Object fromStoreValue(Object storeValue) {
        if (this.allowNullValues && storeValue instanceof NullValue) {
            return null;
        }
        return storeValue;
    }

    /**
     * null转换为NullValue
     *
     * @param userValue 需要转换的
     * @return 转换后的
     */
    protected Object toStoreValue(Object userValue) {
        if (this.allowNullValues && userValue == null) {
            return NullValue.INSTANCE;
        }
        return userValue;
    }


    /**
     * {@link #get(Object, Callable)} 方法加载缓存值的包装异常
     */
    public class LoaderCacheValueException extends RuntimeException {

        private final Object key;

        public LoaderCacheValueException(Object key, Throwable ex) {
            super(String.format("加载key为 %s 的缓存数据,执行被缓存方法异常", JsonUtil.toJson(key)), ex);
            this.key = key;
        }

        public Object getKey() {
            return this.key;
        }
    }

    /**
     * 获取是否开启统计
     *
     * @return true：开启统计，false：关闭统计
     */
    public boolean isStats() {
        return stats;
    }

    /**
     * 获取统计信息
     *
     * @return CacheStats
     */
    @Override
    public CacheStats getCacheStats() {
        return cacheStats;
    }

    public void setCacheStats(CacheStats cacheStats) {
        this.cacheStats = cacheStats;
    }
}
