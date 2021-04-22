package com.y3tu.tool.server.ui.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ui配置
 *
 * @author y3tu
 */
@ConfigurationProperties(prefix = "y3tu.tool.server.ui")
@Data
public class UiProperties {

    /**
     * 访问ui的url
     */
    private String urlPattern;

    /**
     * 登录用户
     */
    private String username;
    /**
     * 登录密码
     */
    private String password;

}
