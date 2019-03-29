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
}
