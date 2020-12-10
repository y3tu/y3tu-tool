package com.y3tu.tool.web.updownload.ftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.StringTokenizer;

/**
 * @author y3tu
 */
@Slf4j
public class FtpHelper {
    private FtpPool ftpPool;

    public FtpHelper(FtpPool ftpPool) {
        this.ftpPool = ftpPool;
    }

    /**
     * 向FTP服务器上传文件
     *
     * @param remoteFile 上传到FTP服务器上的文件路径+文件名
     * @param input      本地文件流
     * @return 成功返回true，否则返回false
     */
    public boolean upload(String remoteFile, InputStream input) {
        boolean result = false;
        FTPClient ftpClient = ftpPool.getFTPClient();
        try {
            ftpClient.enterLocalPassiveMode();
            result = ftpClient.storeFile(remoteFile, input);
            input.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ftpPool.returnFTPClient(ftpClient);
        }
        return result;
    }

    /**
     * 向FTP服务器上传文件
     *
     * @param remoteFile 上传到FTP服务器上的文件路径+文件名
     * @param file       浏览器上传的文件
     * @return
     */
    public boolean upload(String remoteFile, MultipartFile file) {
        boolean result = false;
        try {
            upload(remoteFile, file.getInputStream());
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 向FTP服务器上传文件
     *
     * @param remoteFile 上传到FTP服务器上的文件路径+文件名
     * @param localFile  本地文件路径+文件名
     * @return 成功返回true，否则返回false
     */
    public boolean upload(String remoteFile, String localFile) {
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(localFile));
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return upload(remoteFile, input);
    }

    /**
     * 从FTP服务器下载文件
     *
     * @param remoteFile 远程服务器上的文件路径+文件名
     * @param localFile  本地文件路径+文件名
     * @return 成功返回true，否则返回false
     */
    public boolean download(String remoteFile, String localFile) {
        boolean result = false;
        FTPClient ftpClient = ftpPool.getFTPClient();
        try {
            OutputStream os = new FileOutputStream(localFile);
            ftpClient.retrieveFile(remoteFile, os);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ftpPool.returnFTPClient(ftpClient);
        }
        return result;
    }

    /**
     * 创建文件夹
     *
     * @param remoteDir 文件夹路径
     * @return 如果已经有这个文件夹返回false
     */
    public boolean mkDir(String remoteDir) {
        FTPClient ftpClient = ftpPool.getFTPClient();
        boolean result = false;
        try {
            result = ftpClient.makeDirectory(remoteDir);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            ftpPool.returnFTPClient(ftpClient);
        }
        return result;
    }

    /**
     * 递归创建文件夹.
     *
     * @param dir 文件夹路径
     * @return
     */
    public boolean mkDirs(String dir) {
        boolean result = false;
        if (null == dir) {
            return false;
        }

        FTPClient ftpClient = ftpPool.getFTPClient();
        try {
            ftpClient.changeWorkingDirectory("/");
            StringTokenizer dirs = new StringTokenizer(dir, "/");
            String temp = null;
            while (dirs.hasMoreElements()) {
                temp = dirs.nextElement().toString();
                //创建目录
                ftpClient.makeDirectory(temp);
                //进入目录
                ftpClient.changeWorkingDirectory(temp);
                result = true;
            }
            ftpClient.changeWorkingDirectory("/");

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ftpPool.returnFTPClient(ftpClient);
        }
        return result;
    }

    /**
     * 获取ftp目录下的所有文件
     *
     * @param dir
     * @return
     */
    public FTPFile[] getFiles(String dir) {
        FTPClient ftpClient = ftpPool.getFTPClient();
        FTPFile[] files = new FTPFile[0];
        try {
            files = ftpClient.listFiles(dir);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ftpPool.returnFTPClient(ftpClient);
        }
        return files;
    }

    /**
     * 获取ftp目录下的某种类型的文件
     *
     * @param dir
     * @param filter
     * @return
     */
    public FTPFile[] getFiles(String dir, FTPFileFilter filter) {
        FTPClient ftpClient = ftpPool.getFTPClient();
        FTPFile[] files = new FTPFile[0];
        try {
            files = ftpClient.listFiles(dir, filter);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ftpPool.returnFTPClient(ftpClient);
        }
        return files;
    }

}