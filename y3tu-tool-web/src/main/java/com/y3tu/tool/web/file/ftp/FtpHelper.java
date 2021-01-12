package com.y3tu.tool.web.file.ftp;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.web.file.service.RemoteFileHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.StringTokenizer;

/**
 * @author y3tu
 */
@Slf4j
public class FtpHelper implements RemoteFileHelper {

    private FtpPool ftpPool;

    public FtpHelper(FtpPool ftpPool) {
        this.ftpPool = ftpPool;
    }

    /**
     * 向FTP服务器上传文件
     *
     * @param remotePath 文件路径
     * @param fileName   文件名
     * @param input      本地文件流
     * @return 成功返回true，否则返回false
     */
    @Override
    public boolean upload(String remotePath, String fileName, InputStream input) {
        boolean result = false;
        FTPClient ftpClient = ftpPool.getFTPClient();
        try {
            mkDirs(remotePath);
            ftpClient.enterLocalPassiveMode();
            result = ftpClient.storeFile(remotePath + File.separator + fileName, input);
            input.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (ftpClient != null) {
                ftpPool.returnFTPClient(ftpClient);
            }
        }
        return result;
    }

    /**
     * 向FTP服务器上传文件
     *
     * @param remotePath 上传到FTP服务器上的文件路径
     * @param fileName   文件名
     * @param file       浏览器上传的文件
     * @return
     */
    @Override
    public boolean upload(String remotePath, String fileName, MultipartFile file) {
        boolean result = false;
        try {
            upload(remotePath, fileName, file.getInputStream());
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 向FTP服务器上传文件
     *
     * @param remotePath 上传到FTP服务器上的文件路径
     * @param fileName   文件名
     * @param localFile  本地文件路径+文件名
     * @return 成功返回true，否则返回false
     */
    @Override
    public boolean upload(String remotePath, String fileName, String localFile) {
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(localFile));
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return upload(remotePath, fileName, input);
    }

    /**
     * 从FTP服务器下载文件
     *
     * @param remoteFile 远程服务器上的文件路径+文件名
     * @param localFile  本地文件路径+文件名
     * @return 成功返回true，否则返回false
     */
    @Override
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
            if (ftpClient != null) {
                ftpPool.returnFTPClient(ftpClient);
            }
        }
        return result;
    }

    @Override
    public File download(String remoteFilePath, String fileName, boolean deleteOnExit, HttpServletRequest request, HttpServletResponse response) {
        File destFile = null;
        FTPClient ftpClient = ftpPool.getFTPClient();
        try {
            destFile = new File(FileUtil.SYS_TEM_DIR);
            // 检测是否存在目录
            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    log.warn(" 创建文件夹失败：" + destFile.getParentFile().getAbsolutePath());
                }
            }
            if (!destFile.exists()) {
                OutputStream os = new FileOutputStream(destFile);
                ftpClient.retrieveFile(remoteFilePath, os);
            }
            FileUtil.downloadFile(destFile, fileName, true, request, response);
            if (deleteOnExit) {
                ftpClient.deleteFile(remoteFilePath);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (ftpClient != null) {
                ftpPool.returnFTPClient(ftpClient);
            }
        }
        return destFile;
    }

    @Override
    public boolean remove(String path) {
        boolean result = false;
        FTPClient ftpClient = ftpPool.getFTPClient();
        try {
            ftpClient.deleteFile(path);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (ftpClient != null) {
                ftpPool.returnFTPClient(ftpClient);
            }
        }
        return result;
    }

    /**
     * 创建文件夹
     *
     * @param remoteDir 文件夹路径
     * @return 如果已经有这个文件夹返回false
     */
    @Override
    public boolean mkDir(String remoteDir) {
        FTPClient ftpClient = ftpPool.getFTPClient();
        boolean result = false;
        try {
            result = ftpClient.makeDirectory(remoteDir);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (ftpClient != null) {
                ftpPool.returnFTPClient(ftpClient);
            }
        }
        return result;
    }

    /**
     * 递归创建文件夹
     *
     * @param dir 文件夹路径
     * @return
     */
    @Override
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
            if (ftpClient != null) {
                ftpPool.returnFTPClient(ftpClient);
            }
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
            if (ftpClient != null) {
                ftpPool.returnFTPClient(ftpClient);
            }
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
            if (ftpClient != null) {
                ftpPool.returnFTPClient(ftpClient);
            }
        }
        return files;
    }

}