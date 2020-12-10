package com.y3tu.tool.web.updownload.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Ftp配置
 *
 * @author y3tu
 */
@Data
@ConfigurationProperties(prefix = "y3tu.tool.web.ftp")
public class FtpProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String encoding;
    private String workPath;

    private int maxTotal;
    private int maxWaitMillis;
}
