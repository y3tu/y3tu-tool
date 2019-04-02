package com.y3tu.tool.cache.redis.config;

import com.y3tu.tool.cache.redis.annotation.EnableToolRedis;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Redis工具的bean登记类
 * 主要功能获取注解的值
 *
 * @author y3tu
 * @date 2018/10/22
 */
public class EnableRedisToolRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    /**
     * 是否启用redis切面处理
     */
    public static boolean redisAspectOpen = false;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableToolRedis.class.getName()));
        redisAspectOpen = annoAttrs.getBoolean("aspectOpen");

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        //此处可以扫描工具包里面的包路径加载bean，作用同等于ComponentScan
        // scanner.scan("");
    }
}
