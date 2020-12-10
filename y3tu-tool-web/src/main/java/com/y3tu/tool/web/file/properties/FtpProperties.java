package com.y3tu.tool.web.file.properties;

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
    private String encoding = "UTF-8";
    /**
     * 上传到文件服务器的那个目录
     */
    private String uploadPath;

    /**
     * 池中最多数量的ftp连接
     */
    private int maxTotal;
    /**
     * 连接最长等待时间
     */
    private int maxWaitMillis;
}
