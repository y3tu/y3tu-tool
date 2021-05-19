package com.y3tu.tool.lowcode.report.entity.constant;

/**
 * 远程服务模式
 *
 * @author y3tu
 */
public enum RemoteMode {
    /**
     * SFTP
     */
    SFTP("SFTP"),

    /**
     * FTP
     */
    FTP("FPT"),

    /**
     * 自定义远程服务
     */
    CUSTOMIZE("CUSTOMIZE");

    private String label;

    RemoteMode(String label) {
        this.label = label;
    }
}
