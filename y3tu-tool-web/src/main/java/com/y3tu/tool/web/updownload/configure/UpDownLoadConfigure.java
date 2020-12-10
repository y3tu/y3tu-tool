package com.y3tu.tool.web.updownload.configure;

import com.y3tu.tool.web.updownload.properties.UpDownLoadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 上传相关配置
 *
 * @author y3tu
 */
@Configuration
@EnableConfigurationProperties({UpDownLoadProperties.class})
public class UpDownLoadConfigure {

    @Autowired
    UpDownLoadProperties properties;


}
