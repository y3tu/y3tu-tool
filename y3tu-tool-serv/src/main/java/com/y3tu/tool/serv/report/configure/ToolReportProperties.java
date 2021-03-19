package com.y3tu.tool.serv.report.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author y3tu
 */
@Data
@ConfigurationProperties(prefix = "y3tu.tool.report")
public class ToolReportProperties {
    /**
     * 远程服务器存放模板附件路径
     */
    String remotePath = "/y3tu/report/";
}
