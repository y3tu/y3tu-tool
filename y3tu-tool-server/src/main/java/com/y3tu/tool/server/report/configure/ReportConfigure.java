package com.y3tu.tool.server.report.configure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 报表配置并加载报表bean
 *
 * @author y3tu
 */
@Configuration
@EntityScan(basePackages = {"com.y3tu.tool.serv.report"})
@EnableJpaRepositories(basePackages = {"com.y3tu.tool.server.report"})
@EnableConfigurationProperties(ReportProperties.class)
public class ReportConfigure {
}
