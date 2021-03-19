package com.y3tu.tool.serv.report.configure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 报表配置并加载报表bean
 *
 * @author y3tu
 */
@Configuration
@ComponentScan("com.y3tu.tool.report")
@EntityScan(basePackages = {"com.y3tu.tool.report"})
@EnableJpaRepositories(basePackages = {"com.y3tu.tool.report"})
@EnableConfigurationProperties(ToolReportProperties.class)
public class ToolReportConfigure {
}
