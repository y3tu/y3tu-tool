package com.y3tu.tool.web.updownload.ftp;

import com.y3tu.tool.web.exception.UpDownLoadException;
import com.y3tu.tool.web.updownload.properties.FtpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;
import java.net.SocketException;

/**
 * Ftp连接池工厂
 *
 * @author y3tu
 */
@Slf4j
public class FtpPoolFactory implements PooledObjectFactory<FTPClient> {

    /**
     * ftp配置
     */
    private FtpProperties ftpProperties;
    private static int BUFF_SIZE = 256000;

    public FtpPoolFactory(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    @Override
    public PooledObject<FTPClient> makeObject() throws Exception {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setDefaultPort(ftpProperties.getPort());
        ftpClient.setConnectTimeout(30000);
        ftpClient.setDataTimeout(180000);
        ftpClient.setControlKeepAliveTimeout(60);
        ftpClient.setControlKeepAliveReplyTimeout(60);
        ftpClient.setControlEncoding(ftpProperties.getEncoding());

        FTPClientConfig clientConfig = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
        clientConfig.setServerLanguageCode(ftpProperties.getEncoding());
        ftpClient.configure(clientConfig);

        try {
            ftpClient.connect(ftpProperties.getHost());
        } catch (SocketException exp) {
            log.error("连接FTP服务超时,IP" + ftpProperties.getHost());
            throw new UpDownLoadException(exp.getMessage());
        } catch (IOException exp) {
            log.warn("连接FTP服务:" + ftpProperties.getHost() + " 异常:" + exp.getMessage());
            throw new UpDownLoadException(exp.getMessage());
        }

        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            log.error("FTP服务拒绝连接!");
            return null;
        }
        boolean result = ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword());
        if (!result) {
            log.error("FTP服务拒绝连接!");
            throw new UpDownLoadException(
                    "FTP服务登录失败:" + ftpProperties.getHost() + " 用户名密码有误!");
        }
        ftpClient.setBufferSize(BUFF_SIZE);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.setFileTransferMode(FTP.COMPRESSED_TRANSFER_MODE);
        ftpClient.changeWorkingDirectory(ftpProperties.getWorkPath());
        return new DefaultPooledObject(ftpClient);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> pooledObject) {
        FTPClient ftpClient = pooledObject.getObject();

        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.logout();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new UpDownLoadException("不能断开FTP服务", e);
        } finally {
            // 注意,一定要在finally代码中断开连接，否则会导致占用ftp连接情况
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new UpDownLoadException("不能断开FTP服务", e);
            }
        }

    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> pooledObject) {
        FTPClient ftpClient = pooledObject.getObject();
        try {
            return ftpClient.sendNoOp();
        } catch (IOException e) {
            log.error("验证FTPClient失败:" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<FTPClient> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<FTPClient> pooledObject) throws Exception {

    }

    public FtpProperties getFtpProperties() {
        return this.ftpProperties;
    }
}
