package com.y3tu.tool.report.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author y3tu
 */
@Data
@ConfigurationProperties(prefix = "y3tu.tool.report")
public class ToolReportProperties {
}
