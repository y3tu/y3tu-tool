package com.y3tu.tool.web.file.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;

/**
 * 远程文件处理
 *
 * @author y3tu
 */
public interface RemoteFileHelper {

    /**
     * 上传
     *
     * @param remotePath 文件路径
     * @param fileName   文件名
     * @param input      本地文件流
     * @return 成功返回true，否则返回false
     */
    boolean upload(String remotePath, String fileName, InputStream input);

    /**
     * 上传
     *
     * @param remotePath 文件路径
     * @param fileName   文件名
     * @param file       浏览器上传的文件
     * @return 成功返回true，否则返回false
     */
    boolean upload(String remotePath, String fileName, MultipartFile file);

    /**
     * 上传
     *
     * @param remotePath 文件路径
     * @param fileName   文件名
     * @param localFile  本地文件路径+文件名
     * @return 成功返回true，否则返回false
     */
    boolean upload(String remotePath, String fileName, String localFile);

    /**
     * 下载
     *
     * @param remoteFilePath 远程服务器上的文件路径+文件名
     * @param localFilePath  本地文件路径+文件名
     * @return 成功返回true，否则返回false
     */
    boolean download(String remoteFilePath, String localFilePath);

    /**
     * 浏览器从远程服务器上下载文件
     *
     * @param remoteFilePath 远程服务器上文件全路径
     * @param fileName       文件名
     * @param deleteOnExit   退出时是否删除文件
     * @param request        请求
     * @param response       响应
     * @return
     */
    File download(String remoteFilePath, String fileName, boolean deleteOnExit, HttpServletRequest request, HttpServletResponse response);

    /**
     * 删除远程服务器上文件
     *
     * @param remoteFilePath 远程文件全路径
     * @return
     */
    boolean remove(String remoteFilePath);

    /**
     * 创建文件夹
     *
     * @param remotePath 文件夹路径
     * @return 如果已经有这个文件夹返回false
     */
    boolean mkDir(String remotePath);

    /**
     * 递归创建文件夹
     *
     * @param remotePath 文件夹路径
     * @return 如果已经有这个文件夹返回false
     */
    boolean mkDirs(String remotePath);
}
