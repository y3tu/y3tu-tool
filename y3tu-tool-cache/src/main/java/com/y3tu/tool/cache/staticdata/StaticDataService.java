package com.y3tu.tool.cache.staticdata;

import com.y3tu.tool.cache.annotation.FirstCache;
import com.y3tu.tool.cache.annotation.SecondaryCache;
import com.y3tu.tool.cache.annotation.StaticData;
import com.y3tu.tool.cache.core.cache.Cache;
import com.y3tu.tool.cache.core.manager.CacheManager;
import com.y3tu.tool.cache.core.setting.FirstCacheSetting;
import com.y3tu.tool.cache.core.setting.LayeringCacheSetting;
import com.y3tu.tool.cache.core.setting.SecondaryCacheSetting;
import com.y3tu.tool.cache.properties.CacheProperties;
import com.y3tu.tool.cache.staticdata.dto.StaticDataConfig;
import com.y3tu.tool.cache.staticdata.handler.StaticDataHandler;
import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.core.thread.ThreadUtil;
import com.y3tu.tool.core.util.BeanCacheUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 静态资料收集
 *
 * @author y3tu
 */
@Service
@Data
@Slf4j
public class StaticDataService {

    private final String RESOURCE_PATTERN = "/**/*.class";

    @Autowired
    CacheManager cacheManager;

    @Autowired
    CacheProperties properties;

    @Autowired
    ApplicationContext context;

    ExecutorService executor;

    List<StaticDataConfig> staticDataConfigList;

    public StaticDataService() {
        ThreadFactory factory = ThreadUtil.newNamedThreadFactory("静态资料收集线程池", true);
        executor = ThreadUtil.newFixedExecutor(20, factory);
    }

    /**
     * 开启线程池收集静态资料
     *
     * @param configDtoList 静态数据配置
     */
    public void dataCollect(List<StaticDataConfig> configDtoList) {

        for (StaticDataConfig config : configDtoList) {
            executor.execute(() -> {
                try {
                    log.info("开始加载静态数据：" + config.getName());
                    long startTime = System.currentTimeMillis() / 1000;
                    loadStaticData(config.getName(), config.getLayeringCacheSetting(), config.getClazz(), config.getMethod(), config.getStaticDataHandler());
                    long endTime = System.currentTimeMillis() / 1000;
                    log.info("加载静态数据完成：" + config.getName() + " 耗时：" + (endTime - startTime) + " 秒");
                } catch (Exception e) {
                    log.error("静态数据加载异常:" + e.getMessage(), e);
                }
            });
        }

    }

    /**
     * 根据静态数据名和配置加载静态数据
     *
     * @param cacheName            缓存名
     * @param layeringCacheSetting 缓存配置
     * @return
     */
    public void loadStaticData(String cacheName, LayeringCacheSetting layeringCacheSetting, Class clazz, Method method, Class<? extends StaticDataHandler> handler) {
        try {
            Cache cache = cacheManager.getCache(cacheName, layeringCacheSetting);
            //先从spring容器中获取静态数据处理对象 如果获取不到就主动创建对象调用方法
            Object targetObj;
            if (context.getBean(clazz) != null) {
                targetObj = context.getBean(clazz);
            } else {
                targetObj = BeanCacheUtil.getBean(clazz);
            }
            StaticDataHandler staticDataHandler = BeanCacheUtil.getBean(handler);
            staticDataHandler.handler(cacheName, cache, method.invoke(targetObj));
        } catch (Exception e) {
            throw new ToolException(e.getMessage(), e);
        }
    }


    public List<StaticDataConfig> readHandler() {
        List<StaticDataConfig> staticDataConfigList = new ArrayList<>();
        //spring工具类，可以获取指定路径下的全部类
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {

            List<Resource> resourceList = new ArrayList<>();
            if (properties.getStaticDataPackage() != null) {
                for (String path : properties.getStaticDataPackage()) {
                    //加载所有jar包中的
                    String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(path) + RESOURCE_PATTERN;
                    Resource[] resources = resourcePatternResolver.getResources(pattern);
                    resourceList.addAll(Arrays.asList(resources));
                }
            } else {
                //加载当前工程下的
                String pattern = "classpath:" + RESOURCE_PATTERN;
                Resource[] resources = resourcePatternResolver.getResources(pattern);
                resourceList.addAll(Arrays.asList(resources));
            }

            //MetadataReader 的工厂类
            MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resourceList) {
                //用于读取类信息
                MetadataReader reader = readerFactory.getMetadataReader(resource);
                //扫描到的class
                String className = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);
                Method[] methods = clazz.getDeclaredMethods();
                for (Method method : methods) {
                    //判断是否有指定主解
                    // 获取注解
                    StaticData staticData = AnnotationUtils.findAnnotation(method, StaticData.class);
                    if (staticData != null) {
                        // 从注解中获取缓存配置
                        FirstCache firstCache = staticData.firstCache();
                        SecondaryCache secondaryCache = staticData.secondaryCache();
                        String depict = staticData.depict();
                        LayeringCacheSetting layeringCacheSetting = buildCacheSetting(firstCache, secondaryCache, depict);
                        Class<? extends StaticDataHandler> handler = staticData.handler();
                        StaticDataConfig staticDataConfig = new StaticDataConfig();
                        staticDataConfig.setName(staticData.cacheName());
                        staticDataConfig.setLayeringCacheSetting(layeringCacheSetting);
                        staticDataConfig.setStartUp(staticData.isStartUp());
                        staticDataConfig.setClazz(clazz);
                        staticDataConfig.setMethod(method);
                        staticDataConfig.setStaticDataHandler(handler);
                        staticDataConfigList.add(staticDataConfig);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        this.staticDataConfigList = staticDataConfigList;
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
