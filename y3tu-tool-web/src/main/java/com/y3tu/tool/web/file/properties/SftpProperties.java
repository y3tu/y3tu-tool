package com.y3tu.tool.web.file.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Sftp配置
 *
 * @author y3tu
 */
@Data
@ConfigurationProperties(prefix = "y3tu.tool.web.sftp")
public class SftpProperties {

    /**
     * 是否启动
     */
    private boolean enable = false;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 端口
     */
    private int port;
    /**
     * ip
     */
    private String host;
    /**
     * 上传到文件服务器的那个目录
     */
    private String uploadPath;
    /**
     * 池中最多数量的sftp连接 默认20
     */
    private int maxTotal = 20;
    /**
     * 连接最长等待时间 默认30秒
     */
    private int maxWaitMillis = 30000;
}
