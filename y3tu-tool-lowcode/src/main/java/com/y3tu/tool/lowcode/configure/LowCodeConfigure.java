package com.y3tu.tool.lowcode.configure;

import com.y3tu.tool.lowcode.report.entity.constant.RemoteMode;
import com.y3tu.tool.lowcode.report.exception.ReportException;
import com.y3tu.tool.web.file.ftp.FtpFactory;
import com.y3tu.tool.web.file.ftp.FtpHelper;
import com.y3tu.tool.web.file.ftp.FtpPool;
import com.y3tu.tool.web.file.properties.FtpProperties;
import com.y3tu.tool.web.file.properties.SftpProperties;
import com.y3tu.tool.web.file.service.RemoteFileHelper;
import com.y3tu.tool.web.file.sftp.SftpFactory;
import com.y3tu.tool.web.file.sftp.SftpHelper;
import com.y3tu.tool.web.file.sftp.SftpPool;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 低代码服务配置
 *
 * @author y3tu
 */
@Configuration
@ComponentScan("com.y3tu.tool.lowcode")
@EnableConfigurationProperties(RemoteProperties.class)
public class LowCodeConfigure {

    @Autowired
    RemoteProperties remoteProperties;

    /**
     * 开启WebSocket支持
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    @ConditionalOnMissingBean(RemoteFileHelper.class)
    RemoteFileHelper remoteFileHelper() {
        if (remoteProperties.getRemoteMode() != RemoteMode.CUSTOMIZE) {
            if (remoteProperties.getHost() == null || remoteProperties.getPort() == 0 || remoteProperties.getUsername() == null || remoteProperties.getPassword() == null) {
                throw new ReportException("请检查远程存储服务配置!");
            }
        }
        if (remoteProperties.getRemoteMode() == RemoteMode.SFTP) {
            //创建SFTP连接池和sftpHelper
            SftpProperties sftpProperties = new SftpProperties();
            BeanUtils.copyProperties(remoteProperties, sftpProperties);
            SftpFactory sftpFactory = new SftpFactory(sftpProperties);
            SftpPool sftpPool = new SftpPool(sftpFactory);
            SftpHelper sftpHelper = new SftpHelper(sftpPool);
            return sftpHelper;
        } else if (remoteProperties.getRemoteMode() == RemoteMode.FTP) {
            //创建FTP连接池和ftpHelper
            FtpProperties ftpProperties = new FtpProperties();
            BeanUtils.copyProperties(remoteProperties, ftpProperties);
            FtpFactory ftpFactory = new FtpFactory(ftpProperties);
            FtpPool ftpPool = new FtpPool(ftpFactory);
            FtpHelper ftpHelper = new FtpHelper(ftpPool);
            return ftpHelper;
        } else {
            //自定义模式 如果是自定义模式，用户需要实现RemoteFileHelper接口,并注入到spring容器中
            throw new ReportException("自定义远程存取服务需要实现RemoteFileHelper接口，并注入到spring容器中");
        }
    }

}
