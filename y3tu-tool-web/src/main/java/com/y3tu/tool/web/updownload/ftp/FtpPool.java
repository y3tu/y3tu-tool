package com.y3tu.tool.web.updownload.ftp;

import com.y3tu.tool.web.updownload.properties.FtpProperties;
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
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            log.error("获取FTP连接异常：", e);
            return null;
        }
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
