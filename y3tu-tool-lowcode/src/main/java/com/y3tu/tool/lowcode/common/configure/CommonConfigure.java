package com.y3tu.tool.lowcode.common.configure;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author y3tu
 */
@Configuration
@EntityScan(basePackages = {"com.y3tu.tool.lowcode.common"})
@EnableJpaRepositories(basePackages = {"com.y3tu.tool.lowcode.common"})
public class CommonConfigure {
}
