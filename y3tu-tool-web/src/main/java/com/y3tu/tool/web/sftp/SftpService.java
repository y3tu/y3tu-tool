package com.y3tu.tool.web.sftp;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.web.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SftpService {

    //创建一个map用于存放channel对象
    private static final Map<String, Channel> SFTP_CHANNEL_POOL = new HashMap<String, Channel>();

    private SftpInfo sftpInfo;

    /**
     * 获取sftp连接
     *
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @return
     * @throws JSchException
     */
    public static Channel getChannelSftp(String ip, String port, String user, String pwd) throws JSchException {
        Session session = null;
        Channel channel = null;

        int sftpPort = Integer.parseInt(port);
        String key = ip + "," + port + "," + user + "," + pwd;

        if (SFTP_CHANNEL_POOL.get(key) == null) {
            JSch jsch = new JSch();
            session = jsch.getSession(user, ip, sftpPort);
            session.setPassword(pwd);
            // 设置是否需要确认连接为no
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications",
                    "publickey,keyboard-interactive,password");
            session.connect(6000000);

            channel = session.openChannel("sftp");
            channel.getId();
            channel.connect();
            SFTP_CHANNEL_POOL.put(key, channel);
            log.info("该channel是新连接的，ID为：" + channel.getId());

        } else {
            channel = SFTP_CHANNEL_POOL.get(key);
            session = channel.getSession();
            if (!session.isConnected()) {
                session.connect();
            }
            if (!channel.isConnected()) {
                channel.connect();
            }
            log.info("该channel从map中获取的，ID为：" + channel.getId());
        }
        return channel;

    }

    public static Channel getChannel(String ip, String port, String user, String pwd) {
        Session session = null;
        Channel channel = null;
        int sftpPort = Integer.parseInt(port);
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, ip, sftpPort);
            session.setPassword(pwd);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("PreferredAuthentications",
                    "publickey,keyboard-interactive,password");
            session.connect(60000);

            channel = session.openChannel("sftp");
            channel.connect();
        } catch (Exception e) {
            throw new ToolException("获取SFTP连接异常");
        }
        return channel;

    }

    /**
     * 下载文件-sftp协议.
     *
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @param sftp         sftp连接
     */
    public static boolean download(final String downloadFile, final String saveFile, final ChannelSftp sftp)
            throws Exception {
        FileOutputStream os = null;
        File file = new File(saveFile);
        try {
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            os = new FileOutputStream(file);
            List<String> list = formatPath(downloadFile);
            sftp.get(list.get(0) + list.get(1), os);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            os.close();
        }
        return true;
    }

    /**
     * 上传文件-sftp协议.
     *
     * @param srcFile  源文件
     * @param dir      保存路径
     * @param fileName 保存文件名
     * @param sftp     sftp连接
     */
    private static void uploadFile(final String srcFile, final String dir, final String fileName, final ChannelSftp sftp)
            throws SftpException {
        mkdir(dir, sftp);
        sftp.cd(dir);
        sftp.put(srcFile, fileName);
    }


    /**
     * 上传文件-sftp协议.
     *
     * @param srcFile 源文件路径，/xxx/xx.yy 或 x:/xxx/xxx.yy
     * @param sftp    sftp连接
     * @return 上传成功与否
     * @throws SftpException 异常 localFile directoryFile
     */
    public static boolean uploadFile(final String srcFile, final String directoryFile, final ChannelSftp sftp) throws SftpException {
        File file = new File(srcFile);
        if (file.exists()) {
            List<String> list = formatPath(directoryFile);
            uploadFile(srcFile, list.get(0), list.get(1), sftp);
            return true;
        }
        return false;
    }


    /**
     * 根据路径创建文件夹.
     *
     * @param dir  路径 必须是 /xxx/xxx/ 不能就单独一个/
     * @param sftp sftp连接
     * @throws SftpException 异常
     */
    @SuppressWarnings("null")
    public static boolean mkdir(final String dir, final ChannelSftp sftp) throws SftpException {
        if (dir == null && dir.length() == 0) {
            return false;
        }
        String md = dir.replaceAll("\\\\", "/");
        if (md.indexOf("/") != 0 || md.length() == 1) {
            return false;
        }
        return mkdirs(md, sftp);
    }

    /**
     * 递归创建文件夹.
     *
     * @param dir  路径
     * @param sftp sftp连接
     * @return 是否创建成功
     * @throws SftpException 异常
     */
    private static boolean mkdirs(final String dir, final ChannelSftp sftp) throws SftpException {
        String dirs = dir.substring(1, dir.length() - 1);
        String[] dirArr = dirs.split("/");
        String base = "";
        for (String d : dirArr) {
            base += "/" + d;
            if (dirExist(base + "/", sftp)) {
                continue;
            } else {
                sftp.mkdir(base + "/");
            }
        }
        return true;
    }

    /**
     * 判断文件夹是否存在.
     *
     * @param dir  文件夹路径， /xxx/xxx/
     * @param sftp sftp协议
     * @return 是否存在
     */
    private static boolean dirExist(final String dir, final ChannelSftp sftp) {
        try {
            Vector<?> vector = sftp.ls(dir);
            if (null == vector) {
                return false;
            } else {
                return true;
            }
        } catch (SftpException e) {
            return false;
        }
    }


    /**
     * 格式化路径.
     *
     * @param srcPath 原路径. /xxx/xxx/xxx.yyy 或 X:/xxx/xxx/xxx.yy
     * @return list, 第一个是路径（/xxx/xxx/）,第二个是文件名（xxx.yy）
     */
    public static List<String> formatPath(final String srcPath) {
        List<String> list = new ArrayList<String>(2);
        String repSrc = srcPath.replaceAll("\\\\", "/");
        int firstP = repSrc.indexOf("/");
        int lastP = repSrc.lastIndexOf("/");
        String fileName = lastP + 1 == repSrc.length() ? "" : repSrc.substring(lastP + 1);
        String dir = firstP == -1 ? "" : repSrc.substring(firstP, lastP);
        dir = (dir.length() == 1 ? dir : (dir + "/"));
        list.add(dir);
        list.add(fileName);
        return list;
    }

    @SuppressWarnings("unused")
    private static void exit(final ChannelSftp sftp) {
        sftp.exit();
    }

    /**
     * 获取一个channel
     *
     * @param sftpInfo
     * @return
     */
    public static ChannelSftp getChannelFromPool(SftpInfo sftpInfo) {
        Channel channel = null;

        if (sftpInfo.isPool()) {
            log.info("将使用channel连接池");
            channel = PoolMapUtil.getChannel(sftpInfo);
        } else {
            try {
                log.info("未使用channel连接池");
                channel = getChannelSftp(sftpInfo.getIp(), sftpInfo.getPort(), sftpInfo.getUser(), sftpInfo.getPwd());
            } catch (JSchException e) {
                log.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
        return (ChannelSftp) channel;
    }


    /**
     * 获取一个channel
     *
     * @return
     */
    public ChannelSftp getChannelFromPool() {
        Channel channel = null;

        if (sftpInfo.isPool()) {
            log.info("将使用channel连接池");
            channel = PoolMapUtil.getChannel(sftpInfo);
        } else {
            try {
                log.info("未使用channel连接池");
                channel = getChannelSftp(sftpInfo.getIp(), sftpInfo.getPort(), sftpInfo.getUser(), sftpInfo.getPwd());
            } catch (JSchException e) {
                log.error(e.getMessage(), e);
                throw new ToolException("获取Sftp Channel异常");
            }
        }
        return (ChannelSftp) channel;
    }

    /**
     * 归还Channel给连接池
     *
     * @param channel
     * @return
     */
    public boolean giveBack(Channel channel) {
        if (channel != null) {
            return PoolMapUtil.giveChannel(sftpInfo, channel);
        }
        return false;
    }

    public boolean delete(ChannelSftp channel) {
        channel.exit();
        return PoolMapUtil.deleteChannel(sftpInfo, channel);
    }

    public boolean uploadSftp(MultipartFile file, String filePath, String fileName) {
        InputStream inputStream = null;
        try {
            ChannelSftp sftp = getChannelFromPool();
            inputStream = file.getInputStream();
            mkdir(filePath, sftp);
            sftp.put(inputStream, filePath + fileName, new SftpUploadProgressMonitor(this, sftp, inputStream));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    public File downloadSftp(String filePath, String fileName, boolean deleteOnExit, HttpServletRequest request, HttpServletResponse response) {
        File destFile = null;
        try {
            destFile = new File((sftpInfo.getTempPath().equals("") ? FileUtil.SYS_TEM_DIR : sftpInfo.getTempPath()) + fileName);
            // 检测是否存在目录
            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    System.out.println("was not successful.");
                }
            }

            if (!destFile.exists()) {
                ChannelSftp sftp = getChannelFromPool();
                sftp.get(filePath, FileUtil.getAbsolutePath(destFile), new SftpProgressMonitor(this, sftp));
            }
            FileUtil.downloadFile(destFile, fileName, true, request, response);
            if (deleteOnExit) {
                ChannelSftp sftp = getChannelFromPool();
                sftp.rm(filePath);
                giveBack(sftp);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return destFile;
    }

    public void downloadSftpBatch(Map<String, String> fileListSftpMap, String zipName, boolean deleteOnExit, HttpServletRequest request, HttpServletResponse response) {

        Map<String, File> fileListMap = new HashMap<>();
        try {
            for (String fileName : fileListSftpMap.keySet()) {
                File destFile = new File((sftpInfo.getTempPath().equals("") ? FileUtil.SYS_TEM_DIR : sftpInfo.getTempPath()) + fileName);
                // 检测是否存在目录
                if (!destFile.getParentFile().exists()) {
                    if (!destFile.getParentFile().mkdirs()) {
                        System.out.println("was not successful.");
                    }
                }
                if (!destFile.exists()) {
                    ChannelSftp sftp = getChannelFromPool();
                    sftp.get(fileListSftpMap.get(fileName), FileUtil.getAbsolutePath(destFile), new SftpProgressMonitor(this, sftp));
                }
                fileListMap.put(fileName, destFile);
            }

            FileUtil.downloadFileBatch(fileListMap, zipName, true, request, response);

            if (deleteOnExit) {
                for (String fileName : fileListSftpMap.keySet()) {
                    ChannelSftp sftp = getChannelFromPool();
                    sftp.rm(fileListSftpMap.get(fileName));
                    giveBack(sftp);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public void downloadUnionBatch(Map<String, String> fileListSftpMap, Map<String, File> fileListLocalMap, String zipName, boolean deleteOnExit, HttpServletRequest request, HttpServletResponse response) {
        Map<String, File> fileListUnionMap = new HashMap<>();
        try {

            for (String fileName : fileListSftpMap.keySet()) {
                File destFile = new File((sftpInfo.getTempPath().equals("") ? FileUtil.SYS_TEM_DIR : sftpInfo.getTempPath()) + fileName);
                // 检测是否存在目录
                if (!destFile.getParentFile().exists()) {
                    if (!destFile.getParentFile().mkdirs()) {
                        System.out.println("was not successful.");
                    }
                }
                if (!destFile.exists()) {
                    ChannelSftp sftp = getChannelFromPool();
                    sftp.get(fileListSftpMap.get(fileName), FileUtil.getAbsolutePath(destFile), new SftpProgressMonitor(this, sftp));
                }
                fileListUnionMap.put(fileName, destFile);
            }

            if (fileListLocalMap != null) {
                for (String fileName : fileListLocalMap.keySet()) {
                    File destFile = new File(sftpInfo.getTempPath() + fileName);
                    // 检测是否存在目录
                    if (!destFile.getParentFile().exists()) {
                        if (!destFile.getParentFile().mkdirs()) {
                            System.out.println("was not successful.");
                        }
                    }
                    FileUtil.copy(fileListLocalMap.get(fileName), destFile, false);
                    fileListUnionMap.put(fileName, destFile);
                }
            }

            FileUtil.downloadFileBatch(fileListUnionMap, zipName, true, request, response);

            if (deleteOnExit) {
                for (String fileName : fileListSftpMap.keySet()) {
                    ChannelSftp sftp = getChannelFromPool();
                    sftp.rm(fileListSftpMap.get(fileName));
                    giveBack(sftp);
                }

                for (String fileName : fileListLocalMap.keySet()) {
                    FileUtil.del(fileListLocalMap.get(fileName));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public boolean remove(String path) {
        ChannelSftp sftp = getChannelFromPool();
        try {
            sftp.rm(path);
            return true;
        } catch (SftpException e) {
            return false;
        } finally {
            giveBack(sftp);
        }
    }

    public SftpService(SftpInfo sftpInfo) {
        this.sftpInfo = sftpInfo;
    }

}
