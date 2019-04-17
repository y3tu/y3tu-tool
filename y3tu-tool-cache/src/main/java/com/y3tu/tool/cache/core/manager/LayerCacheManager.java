package com.y3tu.tool.cache.core.manager;


import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.cache.LayerCache;
import com.y3tu.tool.cache.core.cache.caffeine.CaffeineCache;
import com.y3tu.tool.cache.core.cache.redis.RedisCache;
import com.y3tu.tool.cache.core.listener.RedisMessageListener;
import com.y3tu.tool.cache.core.setting.LayerCacheSetting;
import com.y3tu.tool.cache.core.stats.StatsService;
import com.y3tu.tool.core.bean.BeanCache;
import lombok.Data;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.SmartLifecycle;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author yuhao.wang
 */
@Data
public class LayerCacheManager extends AbstractCacheManager implements InitializingBean, BeanNameAware, SmartLifecycle {

    /**
     * redis 客户端
     */
    RedisTemplate<String, Object> redisTemplate;

    /**
     * redis pub/sub 容器
     */
    private final RedisMessageListenerContainer container = new RedisMessageListenerContainer();

    /**
     * redis pub/sub 监听器
     */
    private final RedisMessageListener messageListener = new RedisMessageListener();


    public LayerCacheManager (){

    }

    public LayerCacheManager(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        cacheManagers.add(this);
    }

    @Override
    protected Cache getMissingCache(String name, LayerCacheSetting layerCacheSetting) {
        // 创建一级缓存
        CaffeineCache caffeineCache = new CaffeineCache(name, layerCacheSetting.getFirstCacheSetting(), getStats());
        // 创建二级缓存
        RedisCache redisCache = new RedisCache(name, redisTemplate, layerCacheSetting.getSecondaryCacheSetting(), getStats());
        return new LayerCache(redisTemplate, caffeineCache, redisCache, super.getStats(), layerCacheSetting);
    }

    /**
     * 添加消息监听
     *
     * @param name 缓存名称
     */
    @Override
    protected void addMessageListener(String name) {
        container.addMessageListener(messageListener, new ChannelTopic(name));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        messageListener.setCacheManager(this);
        container.setConnectionFactory(getRedisTemplate().getConnectionFactory());
        container.afterPropertiesSet();
        messageListener.afterPropertiesSet();

        BeanCache.getBean(StatsService.class).setCacheManager(this);
        if (getStats()) {
            // 采集缓存命中率数据
            BeanCache.getBean(StatsService.class).syncCacheStats();
        }
    }

    @Override
    public void setBeanName(String name) {
        container.setBeanName("redisMessageListenerContainer");
    }

    @Override
    public void destroy() throws Exception {
        container.destroy();
        BeanCache.getBean(StatsService.class).shutdownExecutor();
    }

    @Override
    public boolean isAutoStartup() {
        return container.isAutoStartup();
    }

    @Override
    public void stop(Runnable callback) {
        container.stop(callback);
    }

    @Override
    public void start() {
        container.start();
    }

    @Override
    public void stop() {
        container.stop();
    }

    @Override
    public boolean isRunning() {
        return container.isRunning();
    }

    @Override
    public int getPhase() {
        return container.getPhase();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
