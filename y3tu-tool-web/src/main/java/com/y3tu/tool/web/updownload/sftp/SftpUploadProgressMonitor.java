package com.y3tu.tool.web.updownload.sftp;

import com.jcraft.jsch.ChannelSftp;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * sftp上传监控
 *
 * @author y3tu
 */
@Slf4j
public class SftpUploadProgressMonitor implements com.jcraft.jsch.SftpProgressMonitor {

    private ChannelSftp channelSftp;
    private SftpHelper sftpHelper;
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
        sftpHelper.giveBack(channelSftp);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    SftpUploadProgressMonitor(SftpHelper sftpHelper, ChannelSftp channelSftp, InputStream inputStream) {
        this.sftpHelper = sftpHelper;
        this.channelSftp = channelSftp;
        this.inputStream = inputStream;
    }

}
