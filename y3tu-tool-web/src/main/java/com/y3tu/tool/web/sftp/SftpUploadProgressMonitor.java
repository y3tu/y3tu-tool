package com.y3tu.tool.web.sftp;

import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author y3tu
 */
@Slf4j
public class SftpUploadProgressMonitor implements com.jcraft.jsch.SftpProgressMonitor {

    private ChannelSftp channelSftp;
    private SftpService sftpService;
    private InputStream inputStream;

    private long transferSize = 0;

    @Override
    public void init(int op, String src, String dest, long max) {
        log.info("开始传输");
    }

    @Override
    public boolean count(long count) {
        transferSize = transferSize + count;
        return true;
    }

    @Override
    public void end() {
        log.info("完成传输");
        //放回连接池
        sftpService.giveBack(channelSftp);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    SftpUploadProgressMonitor(SftpService sftpService, ChannelSftp channelSftp, InputStream inputStream) {
        this.sftpService = sftpService;
        this.channelSftp = channelSftp;
        this.inputStream = inputStream;
    }

}
