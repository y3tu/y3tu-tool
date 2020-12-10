package com.y3tu.tool.web.updownload.configure;

import com.y3tu.tool.web.updownload.properties.SftpProperties;
import com.y3tu.tool.web.updownload.sftp.SftpFactory;
import com.y3tu.tool.web.updownload.sftp.SftpHelper;
import com.y3tu.tool.web.updownload.sftp.SftpPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("y3tu.tool.web.sftp")
@EnableConfigurationProperties({SftpProperties.class})
public class SftpConfigure {

    /**
     * sftp连接池工厂
     *
     * @return
     */
    @Bean
    SftpFactory sftpFactory(SftpProperties sftpProperties) {
        return new SftpFactory(sftpProperties);
    }

    /**
     * sftp连接池
     *
     * @param sftpFactory
     * @return
     */
    @Bean
    SftpPool ftpPool(SftpFactory sftpFactory) {
        return new SftpPool(sftpFactory);
    }

    @Bean
    SftpHelper sftpHelper(SftpPool sftpPool) {
        return new SftpHelper(sftpPool);
    }

}
