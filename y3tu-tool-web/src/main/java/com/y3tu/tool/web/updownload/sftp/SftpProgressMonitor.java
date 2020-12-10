package com.y3tu.tool.web.updownload.sftp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


/**
 * sftp进度监控
 *
 * @author y3tu
 */
@Slf4j
public class SftpProgressMonitor implements com.jcraft.jsch.SftpProgressMonitor {


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
    }


}
