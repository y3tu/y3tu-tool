package com.y3tu.tool.server.report.configure;

import com.y3tu.tool.server.configure.RemoteProperties;
import com.y3tu.tool.server.report.entity.constant.RemoteMode;
import com.y3tu.tool.server.report.exception.ReportException;
import com.y3tu.tool.web.file.ftp.FtpFactory;
import com.y3tu.tool.web.file.ftp.FtpHelper;
import com.y3tu.tool.web.file.ftp.FtpPool;
import com.y3tu.tool.web.file.properties.FtpProperties;
import com.y3tu.tool.web.file.properties.SftpProperties;
import com.y3tu.tool.web.file.service.RemoteFileHelper;
import com.y3tu.tool.web.file.sftp.SftpFactory;
import com.y3tu.tool.web.file.sftp.SftpHelper;
import com.y3tu.tool.web.file.sftp.SftpPool;
import com.y3tu.tool.web.util.SpringContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 报表配置并加载报表bean
 *
 * @author y3tu
 */
@Configuration
@EntityScan(basePackages = {"com.y3tu.tool.server.report"})
@EnableJpaRepositories(basePackages = {"com.y3tu.tool.server.report"})
@EnableConfigurationProperties(ReportProperties.class)
public class ReportConfigure {

}
