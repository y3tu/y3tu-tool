package com.y3tu.tool.web.file.configure;

import com.y3tu.tool.web.file.properties.SftpProperties;
import com.y3tu.tool.web.file.service.RemoteFileHelper;
import com.y3tu.tool.web.file.sftp.SftpFactory;
import com.y3tu.tool.web.file.sftp.SftpHelper;
import com.y3tu.tool.web.file.sftp.SftpPool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SFTP配置
 *
 * @author y3tu
 */
@Configuration
@ConditionalOnProperty(value = "y3tu.tool.web.sftp.enable",havingValue = "true")
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
    RemoteFileHelper sftpHelper(SftpPool sftpPool) {
        return new SftpHelper(sftpPool);
    }

}
