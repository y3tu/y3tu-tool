package com.y3tu.tool.report.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ReportBeanDefinitionRegistrar.class)
public class ReportConfigure {
}
