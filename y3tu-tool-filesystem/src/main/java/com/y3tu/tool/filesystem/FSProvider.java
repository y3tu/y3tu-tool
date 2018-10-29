package com.y3tu.tool.filesystem;

/**
 * 文件操作接口
 *
 * @author vakin
 */
public interface FSProvider {

    String name();

    /**
     * 文件上传
     *
     * @param object
     * @return
     */
    String upload(UploadObject object);

    /**
     * 获取文件下载地址
     *
     * @param fileKey 文件（全路径或者fileKey）
     * @return
     */
    String getDownloadUrl(String fileKey);

    /**
     * 删除
     *
     * @param fileKey 文件（全路径或者fileKey）
     * @return
     */
    boolean delete(String fileKey);

    /**
     * 下载文件并保存
     *
     * @param fileKey      文件全路径或者fileKey
     * @param localSaveDir 本地保存路径
     * @return
     */
    String downloadAndSaveAs(String fileKey, String localSaveDir);
}
