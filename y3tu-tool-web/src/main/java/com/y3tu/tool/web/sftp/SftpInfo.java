package com.y3tu.tool.web.sftp;

import lombok.AllArgsConstructor;
import lombok.Data;

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