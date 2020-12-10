package com.y3tu.tool.web.updownload.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.y3tu.tool.web.updownload.properties.SftpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Sftp连接池
 *
 * @author y3tu
 */
@Slf4j
public class SftpPool {
    private GenericObjectPool<ChannelSftp> pool;

    public SftpPool(SftpFactory factory) {
        SftpProperties sftpProperties = factory.getSftpProperties();
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // 不设置的话默认是8
        poolConfig.setMaxTotal(sftpProperties.getMaxTotal());
        // 不设置默认无限等待
        poolConfig.setMaxWaitMillis(sftpProperties.getMaxWaitMillis());
        this.pool = new GenericObjectPool<ChannelSftp>(factory, poolConfig);
    }

    /**
     * 获取资源
     *
     * @return
     */
    public ChannelSftp getChannelSftp() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            log.error("获取SFTP连接异常：", e);
            return null;
        }
    }

    /**
     * 归还资源
     *
     * @param channelSftp
     */
    public void returnChannelSftp(ChannelSftp channelSftp) {
        try {
            pool.returnObject(channelSftp);
        } catch (Exception e) {
            log.error("释放SFTP连接异常：", e);
        }
    }

    /**
     * 销毁池子
     */
    public void destroy() {
        try {
            pool.close();
        } catch (Exception e) {
            log.error("销毁SFTP数据源异常：", e);
        }
    }
}
