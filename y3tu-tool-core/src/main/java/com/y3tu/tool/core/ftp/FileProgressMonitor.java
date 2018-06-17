package com.y3tu.tool.core.ftp;

import com.jcraft.jsch.SftpProgressMonitor;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;

/**
 * 文件上传监控
 *
 * @author y3tu
 * @date 2018/4/9
 */
@Slf4j
public class FileProgressMonitor implements SftpProgressMonitor {
    /**
     * 记录已传输的数据总大小
     */
    private long transfered;
    /**
     * 记录文件总大小
     */
    private long fileSize;
    /**
     * 打印日志时间间隔
     */
    private int minInterval = 100;
    /**
     * 开始时间
     */
    private long start;
    private DecimalFormat df = new DecimalFormat("#.##");
    private long preTime;

    /** 传输开始 */
    @Override
    public void init(int op, String src, String dest, long max) {
        this.fileSize = max;
        log.info("Transferring begin.");
        start = System.currentTimeMillis();
    }

    /** 传输中 */
    @Override
    public boolean count(long count) {
        if (fileSize != 0 && transfered == 0) {
            log.info("Transferring progress message: {}%", df.format(0));
            preTime = System.currentTimeMillis();
        }
        transfered += count;
        if (fileSize != 0) {
            long interval = System.currentTimeMillis() - preTime;
            if (transfered == fileSize || interval > minInterval) {
                preTime = System.currentTimeMillis();
                double d = ((double) transfered * 100) / (double) fileSize;
                log.info("Transferring progress message: {}%", df.format(d));
            }
        } else {
            log.info("Transferring progress message: " + transfered);
        }
        return true;
    }

    /** 传输结束 */
    @Override
    public void end() {
        log.info("Transferring end. used time: {}ms", System.currentTimeMillis() - start);
    }
}
