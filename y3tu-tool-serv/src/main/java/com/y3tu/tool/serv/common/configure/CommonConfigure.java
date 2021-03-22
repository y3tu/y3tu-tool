package com.y3tu.tool.serv.common.configure;

import com.y3tu.tool.serv.report.configure.ReportProperties;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author y3tu
 */
@Configuration
@EntityScan(basePackages = {"com.y3tu.tool.serv.common"})
@EnableJpaRepositories(basePackages = {"com.y3tu.tool.serv.common"})
public class CommonConfigure {
}
