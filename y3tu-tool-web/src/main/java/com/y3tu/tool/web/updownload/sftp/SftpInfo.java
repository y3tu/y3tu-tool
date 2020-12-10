package com.y3tu.tool.web.updownload.sftp;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * sftp配置信息
 *
 * @author y3tu
 */
@Data
@AllArgsConstructor
public class SftpInfo {
    String ip;
    String port;
    String user;
    String pwd;
    String tempPath;
    int channelNum;
    boolean isPool;
}
