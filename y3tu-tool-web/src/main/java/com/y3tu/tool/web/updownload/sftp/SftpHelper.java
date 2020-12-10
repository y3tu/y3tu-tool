package com.y3tu.tool.web.updownload.sftp;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.y3tu.tool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sftp服务
 *
 * @author y3tu
 */
@Slf4j
public class SftpHelper {

    private SftpPool sftpPool;

    public SftpHelper(SftpPool sftpPool) {
        this.sftpPool = sftpPool;
    }


    /**
     * 上传文件
     *
     * @param remotePath 远程服务器上文件路径
     * @param fileName   文件名
     * @param file       浏览器上传的文件
     * @return 成功返回true，否则返回false
     */
    public boolean upload(String remotePath, String fileName, MultipartFile file) {
        InputStream inputStream = null;
        ChannelSftp channelSftp = sftpPool.getChannelSftp();
        boolean result = false;
        try {
            inputStream = file.getInputStream();
            mkdir(remotePath);
            channelSftp.put(inputStream, remotePath + fileName, new SftpProgressMonitor());
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            sftpPool.returnChannelSftp(channelSftp);
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 上传文件
     *
     * @param remoteDir 远程服务器上文件路径
     * @param srcFile   本地文件路径全路径
     * @param fileName  保存文件名
     * @return 成功返回true，否则返回false
     */
    private boolean upload(String remoteDir, String srcFile, final String fileName) {
        ChannelSftp channelSftp = sftpPool.getChannelSftp();
        try {
            mkdir(remoteDir);
            channelSftp.cd(remoteDir);
            channelSftp.put(srcFile, fileName);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            sftpPool.returnChannelSftp(channelSftp);
        }
        return true;
    }


    /**
     * 上传文件
     *
     * @param remoteDir 远程服务器上文件路径
     * @param srcFile   本地文件路径全路径 源文件路径，/xxx/xx.yy 或 x:/xxx/xxx.yy
     * @return 上传成功与否
     */
    public boolean upload(String remoteDir, String srcFile) {
        File file = new File(srcFile);
        if (file.exists()) {
            List<String> list = formatPath(remoteDir);
            return upload(srcFile, list.get(0), list.get(1));
        }
        return false;
    }


    /**
     * 下载文件
     *
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @return 成功返回true，否则返回false
     */
    public boolean download(String downloadFile, String saveFile) {
        boolean result = false;
        ChannelSftp channelSftp = sftpPool.getChannelSftp();
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
            channelSftp.get(list.get(0) + list.get(1), os);
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            sftpPool.returnChannelSftp(channelSftp);
            try {
                os.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 浏览器从远程服务器上下载文件
     *
     * @param remotePath   远程服务器上文件路径
     * @param fileName     文件名
     * @param deleteOnExit 退出时是否删除文件
     * @param request      请求
     * @param response     响应
     * @return
     */
    public File download(String remotePath, String fileName, boolean deleteOnExit, HttpServletRequest request, HttpServletResponse response) {
        File destFile = null;
        ChannelSftp channelSftp = sftpPool.getChannelSftp();
        try {
            destFile = new File(FileUtil.SYS_TEM_DIR);
            // 检测是否存在目录
            if (!destFile.getParentFile().exists()) {
                if (!destFile.getParentFile().mkdirs()) {
                    log.warn(" 创建文件夹失败：" + destFile.getParentFile().getAbsolutePath());
                }
            }

            if (!destFile.exists()) {
                channelSftp.get(remotePath, FileUtil.getAbsolutePath(destFile), new SftpProgressMonitor());
            }
            FileUtil.downloadFile(destFile, fileName, true, request, response);
            if (deleteOnExit) {
                channelSftp.rm(remotePath);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            sftpPool.returnChannelSftp(channelSftp);
        }
        return destFile;
    }

    /**
     * 浏览器从远程服务器上批量下载文件并压缩
     *
     * @param fileListSftpMap 远程文件名和路径
     * @param zipName         zip压缩包名
     * @param deleteOnExit    退出时是否删除文件
     * @param request         请求
     * @param response        响应
     */
    public void downloadBatch(Map<String, String> fileListSftpMap, String zipName, boolean deleteOnExit, HttpServletRequest request, HttpServletResponse response) {
        ChannelSftp channelSftp = sftpPool.getChannelSftp();
        Map<String, File> fileListMap = new HashMap<>();
        try {
            for (String fileName : fileListSftpMap.keySet()) {
                File destFile = new File(FileUtil.SYS_TEM_DIR);
                // 检测是否存在目录
                if (!destFile.getParentFile().exists()) {
                    if (!destFile.getParentFile().mkdirs()) {
                        log.warn(" 创建文件夹失败：" + destFile.getParentFile().getAbsolutePath());
                    }
                }
                if (!destFile.exists()) {
                    channelSftp.get(fileListSftpMap.get(fileName), FileUtil.getAbsolutePath(destFile), new SftpProgressMonitor());
                }
                fileListMap.put(fileName, destFile);
            }

            FileUtil.downloadFileBatch(fileListMap, zipName, true, request, response);

            if (deleteOnExit) {
                for (String fileName : fileListSftpMap.keySet()) {
                    channelSftp.rm(fileListSftpMap.get(fileName));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            sftpPool.returnChannelSftp(channelSftp);
        }
    }

    /**
     * 浏览器从远程服务器上批量下载文件并压缩
     *
     * @param fileListSftpMap  远程文件名和路径
     * @param fileListLocalMap 本地文件名和路径
     * @param zipName          zip压缩包名
     * @param deleteOnExit     退出时是否删除文件
     * @param request          请求
     * @param response         响应
     */
    public void downloadUnionBatch(Map<String, String> fileListSftpMap, Map<String, File> fileListLocalMap, String zipName, boolean deleteOnExit, HttpServletRequest request, HttpServletResponse response) {
        ChannelSftp channelSftp = sftpPool.getChannelSftp();
        Map<String, File> fileListUnionMap = new HashMap<>();
        try {

            for (String fileName : fileListSftpMap.keySet()) {
                File destFile = new File(FileUtil.SYS_TEM_DIR);
                // 检测是否存在目录
                if (!destFile.getParentFile().exists()) {
                    if (!destFile.getParentFile().mkdirs()) {
                        log.warn(" 创建文件夹失败：" + destFile.getParentFile().getAbsolutePath());
                    }
                }
                if (!destFile.exists()) {
                    channelSftp.get(fileListSftpMap.get(fileName), FileUtil.getAbsolutePath(destFile), new SftpProgressMonitor());
                }
                fileListUnionMap.put(fileName, destFile);
            }

            if (fileListLocalMap != null) {
                for (String fileName : fileListLocalMap.keySet()) {
                    File destFile = new File(FileUtil.SYS_TEM_DIR);
                    // 检测是否存在目录
                    if (!destFile.getParentFile().exists()) {
                        if (!destFile.getParentFile().mkdirs()) {
                            log.warn(" 创建文件夹失败：" + destFile.getParentFile().getAbsolutePath());
                        }
                    }
                    FileUtil.copy(fileListLocalMap.get(fileName), destFile, false);
                    fileListUnionMap.put(fileName, destFile);
                }
            }

            FileUtil.downloadFileBatch(fileListUnionMap, zipName, true, request, response);

            if (deleteOnExit) {
                for (String fileName : fileListSftpMap.keySet()) {
                    channelSftp.rm(fileListSftpMap.get(fileName));
                }

                for (String fileName : fileListLocalMap.keySet()) {
                    FileUtil.del(fileListLocalMap.get(fileName));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            sftpPool.returnChannelSftp(channelSftp);
        }
    }

    /**
     * 删除服务器上文件
     *
     * @param path 远程服务器上文件路径
     * @return
     */
    public boolean remove(String path) {
        ChannelSftp channelSftp = sftpPool.getChannelSftp();
        try {
            channelSftp.rm(path);
            return true;
        } catch (SftpException e) {
            return false;
        } finally {
            sftpPool.returnChannelSftp(channelSftp);
        }
    }


    /**
     * 根据路径创建文件夹.
     *
     * @param dir 路径 必须是 /xxx/xxx/ 不能就单独一个/
     */
    public boolean mkdir(String dir) {
        if (dir == null && dir.length() == 0) {
            return false;
        }
        String md = dir.replaceAll("\\\\", "/");
        if (md.indexOf("/") != 0 || md.length() == 1) {
            return false;
        }
        return mkdirs(md);
    }

    /**
     * 递归创建文件夹.
     *
     * @param dir 路径
     * @return 是否创建成功
     */
    private boolean mkdirs(String dir) {
        ChannelSftp channelSftp = sftpPool.getChannelSftp();
        try {
            String dirs = dir.substring(1, dir.length() - 1);
            String[] dirArr = dirs.split("/");
            String base = "";
            for (String d : dirArr) {
                base += "/" + d;
                if (dirExist(base + "/")) {
                    continue;
                } else {
                    channelSftp.mkdir(base + "/");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            sftpPool.returnChannelSftp(channelSftp);
        }
        return true;
    }

    /**
     * 判断文件夹是否存在.
     *
     * @param dir 文件夹路径， /xxx/xxx/
     * @return 是否存在
     */
    private boolean dirExist(String dir) {
        ChannelSftp channelSftp = sftpPool.getChannelSftp();
        try {
            Vector<?> vector = channelSftp.ls(dir);
            if (null == vector) {
                return false;
            } else {
                return true;
            }
        } catch (SftpException e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            sftpPool.returnChannelSftp(channelSftp);
        }
    }


    /**
     * 格式化路径.
     *
     * @param srcPath 原路径. /xxx/xxx/xxx.yyy 或 X:/xxx/xxx/xxx.yy
     * @return list, 第一个是路径（/xxx/xxx/）,第二个是文件名（xxx.yy）
     */
    public List<String> formatPath(final String srcPath) {
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

}
