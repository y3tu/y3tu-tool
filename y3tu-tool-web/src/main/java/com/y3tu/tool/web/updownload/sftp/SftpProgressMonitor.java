package com.y3tu.tool.web.updownload.sftp;

import com.jcraft.jsch.ChannelSftp;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;


/**
 * sftp进度监控
 *
 * @author y3tu
 */
@Slf4j
public class SftpProgressMonitor implements com.jcraft.jsch.SftpProgressMonitor {

    private ChannelSftp channelSftp;

    private SftpHelper sftpHelper;

    private HttpServletResponse response;

    private long transferSize = 0;

    @Override
    public void init(int op, String src, String dest, long max) {
        log.info("开始传输");
    }

    @SneakyThrows
    @Override
    public boolean count(long count) {
        transferSize = transferSize + count;
        return true;
    }

    @Override
    public void end() {
        log.info("完成传输");
        //放回连接池
        sftpHelper.giveBack(channelSftp);
    }

    SftpProgressMonitor(SftpHelper sftpHelper, ChannelSftp channelSftp) {
        this.sftpHelper = sftpHelper;
        this.channelSftp = channelSftp;
    }

    SftpProgressMonitor(SftpHelper sftpHelper, ChannelSftp channelSftp, HttpServletResponse response) {
        this.sftpHelper = sftpHelper;
        this.channelSftp = channelSftp;
        this.response = response;
    }
}
