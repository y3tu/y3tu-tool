package com.y3tu.tool.ui.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ui配置
 *
 * @author y3tu
 */
@ConfigurationProperties(prefix = "y3tu.tool.ui")
@Data
public class UiProperties {

    /**
     * 访问ui的url
     */
    private String urlPattern;

    /**
     * 登录用户
     */
    private String loginUsername;
    /**
     * 登录密码
     */
    private String loginPassword;
    /**
     * 白名单
     */
    private String allow;

    /**
     * 黑名单
     */
    private String deny;


}
