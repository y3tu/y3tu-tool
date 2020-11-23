package com.y3tu.tool.report.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("com.y3tu.tool.report")
@EnableConfigurationProperties(ReportProperties.class)
public class ReportConfigure {
}
