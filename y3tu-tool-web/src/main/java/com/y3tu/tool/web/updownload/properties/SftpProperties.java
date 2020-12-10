package com.y3tu.tool.web.updownload.properties;

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
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 文件服务器类型
     */
    private String serverType;
    /**
     * 端口
     */
    private int port;
    /**
     * ip
     */
    private String ip;
    /**
     * 上传到文件服务器的那个目录
     */
    private String uploadPath;
    /**
     * 池中最多数量的sftp连接
     */
    private int maxTotal;
    /**
     * 连接最长等待时间
     */
    private int maxWaitMillis;
}
