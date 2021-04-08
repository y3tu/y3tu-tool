package com.y3tu.tool.web.file.configure;

import com.y3tu.tool.web.file.ftp.FtpHelper;
import com.y3tu.tool.web.file.ftp.FtpPool;
import com.y3tu.tool.web.file.ftp.FtpFactory;
import com.y3tu.tool.web.file.properties.FtpProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FTP配置
 *
 * @author y3tu
 */
@Configuration
@ConditionalOnProperty(value = "y3tu.tool.web.ftp.enable",havingValue = "true")
@EnableConfigurationProperties({FtpProperties.class})
public class FtpConfigure {

    /**
     * ftp连接池工厂
     *
     * @return
     */
    @Bean
    FtpFactory ftpPoolFactory(FtpProperties ftpProperties) {
        return new FtpFactory(ftpProperties);
    }

    /**
     * ftp连接池
     *
     * @param ftpFactory
     * @return
     */
    @Bean
    FtpPool ftpPool(FtpFactory ftpFactory) {
        return new FtpPool(ftpFactory);
    }

    @Bean
    FtpHelper ftpHelper(FtpPool ftpPool) {
        return new FtpHelper(ftpPool);
    }
}
