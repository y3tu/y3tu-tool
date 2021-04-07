package com.y3tu.tool.web.file.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.y3tu.tool.web.exception.UpDownLoadException;
import com.y3tu.tool.web.file.properties.SftpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Properties;

/**
 * Sftp工厂
 *
 * @author y3tu
 */
@Slf4j
public class SftpFactory implements PooledObjectFactory<ChannelSftp> {

    /**
     * sftp配置
     */
    private SftpProperties sftpProperties;

    public SftpFactory(SftpProperties sftpProperties) {
        this.sftpProperties = sftpProperties;
    }


    @Override
    public PooledObject<ChannelSftp> makeObject() throws Exception {
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(sftpProperties.getUsername(), sftpProperties.getHost(), sftpProperties.getPort());
            sshSession.setPassword(sftpProperties.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            ChannelSftp channelSftp = (ChannelSftp) sshSession.openChannel("sftp");
            channelSftp.connect();
            log.info("新建sftp连接！");
            return new DefaultPooledObject<>(channelSftp);
        } catch (JSchException e) {
            throw new UpDownLoadException("连接sftp失败", e);
        }
    }

    @Override
    public void destroyObject(PooledObject<ChannelSftp> pooledObject) throws Exception {
        ChannelSftp channelSftp = pooledObject.getObject();
        Session session = channelSftp.getSession();
        channelSftp.disconnect();
        //断开sftp连接之后，再断开session连接
        if (session != null) {
            session.disconnect();
        }
    }

    @Override
    public boolean validateObject(PooledObject<ChannelSftp> pooledObject) {
        try {
            ChannelSftp channelSftp = pooledObject.getObject();
            channelSftp.cd("/");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<ChannelSftp> pooledObject) throws Exception {
        ChannelSftp channelSftp = pooledObject.getObject();
        channelSftp.cd("/");
    }

    @Override
    public void passivateObject(PooledObject<ChannelSftp> pooledObject) throws Exception {
        ChannelSftp channelSftp = pooledObject.getObject();
        channelSftp.cd("/");
    }

    public SftpProperties getSftpProperties() {
        return sftpProperties;
    }
}
