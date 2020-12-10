package com.y3tu.tool.web.updownload.configure;

import com.y3tu.tool.web.updownload.ftp.FtpHelper;
import com.y3tu.tool.web.updownload.ftp.FtpPool;
import com.y3tu.tool.web.updownload.ftp.FtpPoolFactory;
import com.y3tu.tool.web.updownload.properties.FtpProperties;
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
@ConditionalOnProperty("y3tu.tool.web.ftp")
@EnableConfigurationProperties({FtpProperties.class})
public class FtpConfigure {

    /**
     * ftp连接池工厂
     *
     * @return
     */
    @Bean
    FtpPoolFactory ftpPoolFactory(FtpProperties ftpProperties) {
        return new FtpPoolFactory(ftpProperties);
    }

    /**
     * ftp连接池
     *
     * @param ftpPoolFactory
     * @return
     */
    @Bean
    FtpPool ftpPool(FtpPoolFactory ftpPoolFactory) {
        return new FtpPool(ftpPoolFactory);
    }

    @Bean
    FtpHelper ftpHelper(FtpPool ftpPool) {
        return new FtpHelper(ftpPool);
    }
}
