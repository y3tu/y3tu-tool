package com.y3tu.tool.web.file.ftp;

import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.web.file.properties.FtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Ftp连接池
 *
 * @author y3tu
 */
@Slf4j
public class FtpPool {

    private final GenericObjectPool<FTPClient> pool;

    public FtpPool(FtpFactory factory) {
        FtpProperties ftpProperties = factory.getFtpProperties();
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // 不设置的话默认是8
        poolConfig.setMaxTotal(ftpProperties.getMaxTotal());
        // 不设置默认无限等待
        poolConfig.setMaxWaitMillis(ftpProperties.getMaxWaitMillis());
        this.pool = new GenericObjectPool<FTPClient>(factory, poolConfig);
    }

    /**
     * 获取资源
     *
     * @return
     */
    public FTPClient getFTPClient() {
        FTPClient ftpClient = null;
        try {
            ftpClient = pool.borrowObject();
        } catch (Exception e) {
            log.error("获取FTP连接异常：", e);
            throw new ToolException("获取FTP连接异常:" + e.getMessage());
        }
        if (ftpClient == null) {
            throw new ToolException("当前还没有空闲的FTP连接，请稍后再试！");
        }
        return ftpClient;
    }

    /**
     * 归还资源
     *
     * @param ftpClient
     */
    public void returnFTPClient(FTPClient ftpClient) {
        try {
            pool.returnObject(ftpClient);
        } catch (Exception e) {
            log.error("释放FTP连接异常：", e);
        }
    }

    /**
     * 销毁池子
     */
    public void destroy() {
        try {
            pool.close();
        } catch (Exception e) {
            log.error("销毁FTP数据源异常：", e);
        }
    }
}
