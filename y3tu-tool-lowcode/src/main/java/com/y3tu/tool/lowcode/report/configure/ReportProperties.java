package com.y3tu.tool.lowcode.report.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author y3tu
 */
@Data
@ConfigurationProperties(prefix = "y3tu.tool.lowcode.report")
public class ReportProperties {
    /**
     * 远程服务器存放模板附件路径
     */
    String templateRemotePath = "/y3tu/report/";
    /**
     * 远程服务器存放报表路径
     */
    String reportRemotePath = "/y3tu/report/";
}
