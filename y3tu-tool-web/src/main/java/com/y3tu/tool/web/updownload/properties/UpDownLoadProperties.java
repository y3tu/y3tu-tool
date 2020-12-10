package com.y3tu.tool.web.updownload.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 上传相关配置
 *
 * @author y3tu
 */
@Data
@ConfigurationProperties(prefix = "y3tu.tool.web.up")
public class UpDownLoadProperties {

    /**
     * 文件大小限制
     */
    private Long maxSize;

    private Long qiniuMaxSize;

    /**
     * 头像大小限制
     */
    private Long avatarMaxSize;


}
