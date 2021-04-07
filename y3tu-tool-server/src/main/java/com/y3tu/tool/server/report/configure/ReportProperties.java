package com.y3tu.tool.server.report.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author y3tu
 */
@Data
@ConfigurationProperties(prefix = "y3tu.tool.report")
public class ReportProperties {
    /**
     * 远程服务器存放模板附件路径
     */
    String remotePath = "/y3tu/report/";
}