package com.y3tu.tool.web.sftp;

import com.jcraft.jsch.ChannelSftp;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;


/**
 * @author y3tu
 */
@Slf4j
public class SftpProgressMonitor implements com.jcraft.jsch.SftpProgressMonitor {

    private ChannelSftp channelSftp;

    private SftpService sftpService;

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
        response.flushBuffer();
        return true;
    }

    @Override
    public void end() {
        log.info("完成传输");
        //放回连接池
        sftpService.giveBack(channelSftp);
    }

    SftpProgressMonitor(SftpService sftpService, ChannelSftp channelSftp) {
        this.sftpService = sftpService;
        this.channelSftp = channelSftp;
    }

    SftpProgressMonitor(SftpService sftpService, ChannelSftp channelSftp, HttpServletResponse response) {
        this.sftpService = sftpService;
        this.channelSftp = channelSftp;
        this.response = response;
    }
}
