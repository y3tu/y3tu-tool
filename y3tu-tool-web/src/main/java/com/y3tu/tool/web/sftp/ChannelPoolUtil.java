package com.y3tu.tool.web.sftp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class ChannelPoolUtil {

    private int channelNum = 10;
    // 正在执行状态的channel池
    private Map<Integer, Channel> runningChannelPool = new HashMap<>(channelNum);
    // 空闲状态的channel池
    private Map<Integer, Channel> sleepChannelPool = new HashMap<>(channelNum);


    /**
     * 获取Channel
     * @param sftpInfo
     * @return
     * @throws JSchException
     */
    public synchronized Channel getChannel(SftpInfo sftpInfo) throws JSchException {
        Channel channel = null;
        log.info("当前空闲池里有：" + sleepChannelPool.size() + "个channel");
        // 判断SleepPool中是否有channel
        if (sleepChannelPool.size() == 0) {
            // 判断已连接channel总数是否大于指定数量，否则创建新的channel,并返回
            if ((sleepChannelPool.size() + runningChannelPool.size()) < channelNum) {
                channel = SftpService.getChannel(sftpInfo.getIp(),
                        sftpInfo.getPort(), sftpInfo.getUser(),
                        sftpInfo.getPwd());
                log.info("创建了一个新的channel,ID为："
                        + String.valueOf(channel.getId()));
                runningChannelPool.put(channel.getId(), channel);
                log.info("新channel已塞入到：runningChannelPool");
                return channel;
            } else {
                log.info("warning!!暂时无空闲的channel");
                return null;
            }
        } else {
            // 从sleepPool中随机获取一个channel
            Integer[] keys = sleepChannelPool.keySet().toArray(new Integer[0]);
            Integer channelId = keys[0];
            channel = sleepChannelPool.get(channelId);
            System.out.println("从池中获取到channel,ID为：" + channelId);
            // 检查channel是否关闭，重连
            if (channel.isClosed()) {
                Session session;
                try {
                    session = channel.getSession();
                    if (!session.isConnected()) {
                        session.connect();
                    }
                    if (!channel.isConnected()) {
                        channel.connect();
                    }
                } catch (JSchException e) {
                    log.info(e.getMessage(), e);
                    log.info("channel已关闭且重连出错！将其移出池并关闭！");
                    sleepChannelPool.remove(channelId);
                    channel.getSession().disconnect();

                    channel = SftpService.getChannel(sftpInfo.getIp(),
                            sftpInfo.getPort(), sftpInfo.getUser(),
                            sftpInfo.getPwd());
                    log.info("创建了一个新的channel,ID为："
                            + String.valueOf(channel.getId()));
                    runningChannelPool.put(channel.getId(), channel);
                    log.info("新channel已塞入到：runningChannelPool");
                    return channel;
                }
            }
            // 将channel从sleepPool挪入runningPool
            sleepChannelPool.remove(channelId);
            runningChannelPool.put(channelId, channel);
            log.info("获取channel成功！并完成sleepPool-->runningPool");
        }
        return channel;
    }

    /**
     * 将使用完的channel放回sleepPool
     *
     * @param channel
     * @return
     */
    public synchronized boolean giveChannel(Channel channel) {
        runningChannelPool.remove(channel.getId());
        sleepChannelPool.put(channel.getId(), channel);
        log.info("归还channel成功！并完成runningPool-->sleepPool");
        return true;
    }

    /**
     * 彻底删除channel
     * @param channel
     * @return
     */
    public synchronized boolean delete(Channel channel){
        runningChannelPool.remove(channel.getId());
        sleepChannelPool.remove(channel.getId());
        return true;
    }

    public ChannelPoolUtil() {
        super();
    }

    public ChannelPoolUtil(int channelNum,
                           Map<Integer, Channel> runningChannelPool,
                           Map<Integer, Channel> sleepChannelPool) {
        super();
        this.channelNum = channelNum;
        this.runningChannelPool = runningChannelPool;
        this.sleepChannelPool = sleepChannelPool;
    }

}
