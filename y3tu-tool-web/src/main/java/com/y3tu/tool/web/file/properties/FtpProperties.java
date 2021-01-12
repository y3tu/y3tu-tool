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
    /**
     * 是否启动
     */
    private boolean enable = false;
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
     * 池中最多数量的ftp连接 默认20
     */
    private int maxTotal = 20;
    /**
     * 连接最长等待时间 默认30秒
     */
    private int maxWaitMillis = 30000;
}
