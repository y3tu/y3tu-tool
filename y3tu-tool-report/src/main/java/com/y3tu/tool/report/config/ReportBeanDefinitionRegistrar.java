package com.y3tu.tool.report.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;


public class ReportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry) {


        String basePackages = "com.y3tu.tool.report.rest";

        //扫描类
        ClassPathBeanDefinitionScanner  scanner =
                new ClassPathBeanDefinitionScanner(registry, false);
        scanner.scan(basePackages);
    }

}
