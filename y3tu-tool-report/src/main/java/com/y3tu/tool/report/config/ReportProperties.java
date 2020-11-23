package com.y3tu.tool.report.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "y3tu.tool.report")
public class ReportProperties {
}
