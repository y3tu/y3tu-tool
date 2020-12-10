package com.y3tu.tool.web.updownload.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.y3tu.tool.web.exception.UpDownLoadException;
import com.y3tu.tool.web.updownload.properties.SftpProperties;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Properties;

/**
 * @author y3tu
 */
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
            return new DefaultPooledObject<>(channelSftp);
        } catch (JSchException e) {
            throw new UpDownLoadException("连接sftp失败", e);
        }
    }

    @Override
    public void destroyObject(PooledObject<ChannelSftp> pooledObject) throws Exception {
        ChannelSftp channelSftp = pooledObject.getObject();
        channelSftp.disconnect();
    }

    @Override
    public boolean validateObject(PooledObject<ChannelSftp> pooledObject) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<ChannelSftp> pooledObject) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<ChannelSftp> pooledObject) throws Exception {

    }

    public SftpProperties getSftpProperties() {
        return sftpProperties;
    }
}
