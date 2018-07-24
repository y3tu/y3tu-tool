package com.y3tu.tool.ssh.ftp;

/**
 * @author y3tu
 * @date 2018/4/9
 */
public class FtpException extends Exception {

    public FtpException(String message) {
        super(message);
    }

    public FtpException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
