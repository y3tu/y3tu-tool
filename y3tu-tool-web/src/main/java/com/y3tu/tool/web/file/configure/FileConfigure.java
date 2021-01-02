package com.y3tu.tool.web.file.configure;

import com.y3tu.tool.web.file.properties.FileProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 上传相关配置
 *
 * @author y3tu
 */
@Configuration
@EnableConfigurationProperties({FileProperties.class})
public class FileConfigure {

    @Autowired
    FileProperties properties;

}
