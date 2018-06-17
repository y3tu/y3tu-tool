package com.y3tu.tool.core.ftp;

import com.jcraft.jsch.*;

import com.y3tu.tool.core.exception.FtpException;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * Sftp客户端
 *
 * @author y3tu
 * @date 2018/4/9
 */
@Slf4j
public class SftpClient {
    private Session session = null;
    private ChannelSftp channel = null;

    private SftpClient() {
    }

    /**
     * 获取配置的默认连接
     *
     * @return
     * @throws FtpException
     */
//    public static SftpClient connectDefault() throws FtpException {
//        String host = PropertiesUtil.getString("sftp.host");
//        int port = PropertiesUtil.getInt("sftp.port");
//        String userName = PropertiesUtil.getString("sftp.user.name");
//        String password = PropertiesUtil.getString("sftp.user.password");
//        int timeout = PropertiesUtil.getInt("sftp.timeout");
//        int aliveMax = PropertiesUtil.getInt("sftp.aliveMax");
//        return new SftpClient().init(host, port, userName, password, timeout, aliveMax);
//    }

    /**
     * 获取sftp连接客户端
     *
     * @param host
     * @param port
     * @param userName
     * @param password
     * @param timeout
     * @param aliveMax
     * @return
     * @throws FtpException
     */
    public static SftpClient connect(String host, int port, String userName, String password, int timeout, int aliveMax) throws FtpException {
        return new SftpClient().init(host, port, userName, password, timeout, aliveMax);
    }

    public SftpClient init(String host, int port, String userName, String password, int timeout, int aliveMax) throws FtpException {
        try {
            Properties config = new Properties();

            // 创建JSch对象
            JSch jsch = new JSch();
            // 根据用户名，主机ip，端口获取一个Session对象
            session = jsch.getSession(userName, host, port);
            if (password != null) {
                // 设置密码
                session.setPassword(password);
            }
            config.put("userauth.gssapi-with-mic", "no");
            config.put("StrictHostKeyChecking", "no");
            // 为Session对象设置properties
            session.setConfig(config);
            // 设置timeout时间
            session.setTimeout(timeout);
            session.setServerAliveCountMax(aliveMax);
            // 通过Session建立链接
            session.connect();
            // 打开SFTP通道
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect(); // 建立SFTP通道的连接
            log.info("SSH Channel connected.");
        } catch (JSchException e) {
            throw new FtpException("", e);
        }
        return this;
    }

    public void disconnect() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
            log.info("SSH Channel disconnected.");
        }
    }

    /**
     * 发送文件
     *
     * @param src 本地服务器文件目录地址
     * @param dst 远程文件目录地址
     */
    public void put(String src, String dst) throws FtpException {
        try {
            channel.put(src, dst, new FileProgressMonitor());
        } catch (SftpException e) {
            throw new FtpException("", e);
        }
    }

    /**
     * 获取文件
     *
     * @param src 远程文件目录地址
     * @param dst 本地服务器文件目录地址
     */
    public void get(String src, String dst) throws FtpException {
        try {
            channel.get(src, dst, new FileProgressMonitor());
        } catch (SftpException e) {
            throw new FtpException("", e);
        }
    }

    /**
     * 测试
     *
     * @param args
     */
//    public static void main(String[] args) {
//        String host = "104.224.153.202";
//        int port = 27506;
//        String userName = "root";
//        String password = "rKkgQpb3wxfd";
//        int timeout = 2000;
//        int aliveMax = 2000;
//        try {
//            SftpClient sftpClient = SftpClient.connect(host, port, userName, password, timeout, aliveMax);
//            sftpClient.put("/Users/yxy/Downloads/bootstrap3-dialog-1.35.4.zip", "/y3tu");
//            sftpClient.disconnect();
//
//        } catch (FtpException e) {
//            e.printStackTrace();
//        }
//    }
}
