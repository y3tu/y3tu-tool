package com.y3tu.tool.cache.staticdata;

import com.y3tu.tool.cache.annotation.FirstCache;
import com.y3tu.tool.cache.annotation.SecondaryCache;
import com.y3tu.tool.cache.annotation.StaticData;
import com.y3tu.tool.cache.core.setting.FirstCacheSetting;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import com.y3tu.tool.cache.core.setting.SecondaryCacheSetting;
import com.y3tu.tool.cache.service.ToolCacheService;
import com.y3tu.tool.core.thread.ThreadUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 静态资料收集
 */
@Data
@Slf4j
public class StaticDataCollect {

    private final String RESOURCE_PATTERN = "/**/*.class";

    ToolCacheService cacheService;

    private String handlerPackage;

    ExecutorService executor;


    public StaticDataCollect(ToolCacheService cacheService) {
        this.cacheService = cacheService;
        ThreadFactory factory = ThreadUtil.newNamedThreadFactory("静态资料收集线程池", true);
        executor = ThreadUtil.newFixedExecutor(20, factory);
    }

    /**
     * 开启线程池收集静态资料
     *
     * @param configDtoList
     */
    public void dataCollect(List<StaticDataConfig> configDtoList) {

        for (StaticDataConfig config : configDtoList) {
            executor.execute(() -> {
                try {
                    log.info("开始加载静态数据：" + config.getName());
                    cacheService.loadStaticData(config.getName(), config.getLayeringCacheSetting(), config.getClazz(), config.getMethod());
                    log.info("加载静态数据完成：" + config.getName());
                } catch (Exception e) {
                    log.error("静态数据加载异常:" + e.getMessage(), e);
                }
            });
        }

    }

    public List<StaticDataConfig> readHandler() {
        List<StaticDataConfig> staticDataConfigList = new ArrayList<>();
        //spring工具类，可以获取指定路径下的全部类
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(handlerPackage) + RESOURCE_PATTERN;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            //MetadataReader 的工厂类
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                //用于读取类信息
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                //扫描到的class
                String classname = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(classname);
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    //判断是否有指定主解
                    StaticData staticData = method.getAnnotation(StaticData.class);
                    if (staticData != null && staticData.isStartUp()) {
                        // 从注解中获取缓存配置
                        FirstCache firstCache = staticData.firstCache();
                        SecondaryCache secondaryCache = staticData.secondaryCache();
                        String depict = staticData.depict();
                        LayeringCacheSetting layeringCacheSetting = buildCacheSetting(firstCache, secondaryCache, depict);
                        StaticDataConfig staticDataConfig = new StaticDataConfig();
                        staticDataConfig.setName(staticData.cacheName());
                        staticDataConfig.setLayeringCacheSetting(layeringCacheSetting);
                        staticDataConfig.setClazz(clazz);
                        staticDataConfig.setMethod(method);
                        staticDataConfigList.add(staticDataConfig);
                    }
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return staticDataConfigList;
    }

    /**
     * 构建多级缓存配置项
     *
     * @param firstCache     一级缓存配置
     * @param secondaryCache 二级缓存配置
     * @param depict         描述
     * @return
     */
    private LayeringCacheSetting buildCacheSetting(FirstCache firstCache, SecondaryCache secondaryCache, String depict) {
        FirstCacheSetting firstCacheSetting = new FirstCacheSetting(firstCache.initialCapacity(), firstCache.maximumSize(),
                firstCache.expireTime(), firstCache.timeUnit(), firstCache.expireMode());

        SecondaryCacheSetting secondaryCacheSetting = new SecondaryCacheSetting(secondaryCache.expireTime(),
                secondaryCache.preloadTime(), secondaryCache.timeUnit(), secondaryCache.forceRefresh(),
                secondaryCache.isAllowNullValue(), secondaryCache.magnification());

        LayeringCacheSetting layeringCacheSetting = new LayeringCacheSetting(firstCacheSetting, secondaryCacheSetting, depict);
        return layeringCacheSetting;
    }
}
