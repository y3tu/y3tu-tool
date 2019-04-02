package com.y3tu.tool.ui.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @author y3tu
 * @date 2019-03-29
 */
@ConfigurationProperties(prefix = "y3tu.tool.ui")
@Data
public class UiProperties {

    /**
     * 设置拦截的url
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