package com.y3tu.tool.server.configure;

import com.y3tu.tool.server.report.entity.constant.RemoteMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 远程存储服务配置
 *
 * @author y3tu
 */
@Data
@ConfigurationProperties(prefix = "y3tu.tool.server.remote")
public class RemoteProperties {

    /**
     * 远程服务模式 默认SFTP
     */
    RemoteMode remoteMode = RemoteMode.SFTP;
    /**
     * ip
     */
    private String host;
    /**
     * 端口
     */
    private int port;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 池中最多数量的sftp连接 默认20
     */
    private int maxTotal = 20;
    /**
     * 连接最长等待时间 默认30秒
     */
    private int maxWaitMillis = 30000;
}
