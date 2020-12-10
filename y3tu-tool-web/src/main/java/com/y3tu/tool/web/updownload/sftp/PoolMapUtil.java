package com.y3tu.tool.web.updownload.sftp;

import com.jcraft.jsch.Channel;
import com.y3tu.tool.core.exception.ToolException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 池管理
 *
 * @author y3tu
 */
@Slf4j
public class PoolMapUtil {

    private static Map<String, ChannelPoolUtil> poolMap = new HashMap<>();
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 取一个channel,并管理poolMap
     *
     * @param sftpInfo
     * @return
     */
    public static Channel getChannel(SftpInfo sftpInfo) {
        Channel channel = null;
        ChannelPoolUtil channelPool = null;

        Map<Integer, Channel> runningChannelPool = new HashMap<>(sftpInfo.getChannelNum());
        //空闲状态的channel池
        Map<Integer, Channel> sleepChannelPool = new HashMap<>(sftpInfo.getChannelNum());

        // ip+port+user 为 key
        String poolId = sftpInfo.getIp() + sftpInfo.getPort() + sftpInfo.getUser() + sftpInfo.getPwd();
        // 写锁（不支持同时读/写）
        lock.writeLock().lock();
        // 如果该池不存在
        if (poolMap.get(poolId) == null) {
            // 创建一个新pool
            channelPool = new ChannelPoolUtil(sftpInfo.getChannelNum(), runningChannelPool, sleepChannelPool);
            // 放入poolMap
            poolMap.put(poolId, channelPool);
            log.info("创建了一个新channel连接池，poolId为：" + poolId);
        } else {
            channelPool = poolMap.get(poolId);
        }
        // 释放读锁
        lock.writeLock().unlock();
        try {
            channel = channelPool.getChannel(sftpInfo);
        } catch (Exception e) {
            throw new ToolException("获取SFTP连接异常");
        }

        return channel;
    }

    /**
     * 归还channel
     *
     * @param sftpInfo
     * @param channel
     * @return
     */
    public static boolean giveChannel(SftpInfo sftpInfo, Channel channel) {
        String poolId = sftpInfo.getIp() + sftpInfo.getPort() + sftpInfo.getUser() + sftpInfo.getPwd();
        //获取channelPool
        ChannelPoolUtil channelPool = poolMap.get(poolId);
        channelPool.giveChannel(channel);
        return true;
    }

    public static boolean deleteChannel(SftpInfo sftpInfo, Channel channel) {
        String poolId = sftpInfo.getIp() + sftpInfo.getPort() + sftpInfo.getUser() + sftpInfo.getPwd();
        //获取channelPool
        ChannelPoolUtil channelPool = poolMap.get(poolId);
        channelPool.delete(channel);
        return true;
    }

}
