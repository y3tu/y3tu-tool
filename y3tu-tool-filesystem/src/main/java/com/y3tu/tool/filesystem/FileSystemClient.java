package com.y3tu.tool.filesystem;

import com.y3tu.tool.filesystem.provider.ProviderTypeEnum;
import com.y3tu.tool.filesystem.provider.aliyun.AliyunProvider;
import com.y3tu.tool.filesystem.provider.fastdfs.FdfsProvider;
import com.y3tu.tool.filesystem.provider.qiniu.QiniuProvider;
import com.y3tu.tool.setting.Setting;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件系统操作客户端
 *
 * @author y3tu
 */
public class FileSystemClient {

    private static Map<String, FileSystemClient> clients = new HashMap<>();
    private FSProvider fsProvider;


    public FileSystemClient(FSProvider fsProvider) {
        this.fsProvider = fsProvider;
    }

    public static FileSystemClient createClient(ProviderTypeEnum typeEnum, Setting setting) {

        FileSystemClient client = clients.get(typeEnum.getType());
        if (client != null) {
            return client;
        }

        synchronized (clients) {
            client = clients.get(typeEnum.getType());
            if (client != null) {
                return client;
            }
            if (typeEnum == ProviderTypeEnum.ALIYUN) {
                //阿里云
                client = new FileSystemClient(AliyunProvider.createBySetting(setting));
            } else if (typeEnum == ProviderTypeEnum.QINIU) {
                //七牛云
                client = new FileSystemClient(QiniuProvider.createBySetting(setting));
            } else if (typeEnum == ProviderTypeEnum.FASTDFS) {
                //fastDFS
                client = new FileSystemClient(new FdfsProvider(setting.getProperties("")));
            } else if(typeEnum==ProviderTypeEnum.TENCENT){
                //todo 腾讯云

            }
            clients.put(typeEnum.getType(), client);
        }

        return client;
    }


    public String upload(File file) {
        return fsProvider.upload(new UploadObject(file));
    }

    public String upload(String fileKey, File file) {
        return fsProvider.upload(new UploadObject(fileKey, file));
    }

    public String upload(String fileKey, File file, String catalog) {
        return fsProvider.upload(new UploadObject(fileKey, file).setCatalog(catalog));
    }

    public String upload(String fileKey, byte[] contents) {
        return fsProvider.upload(new UploadObject(fileKey, contents));
    }

    public String upload(String fileKey, byte[] contents, String catalog) {
        return fsProvider.upload(new UploadObject(fileKey, contents).setCatalog(catalog));
    }

    public String upload(String fileKey, InputStream in, String mimeType) {
        return fsProvider.upload(new UploadObject(fileKey, in, mimeType));
    }

    public String upload(String fileKey, InputStream in, String mimeType, String catalog) {
        return fsProvider.upload(new UploadObject(fileKey, in, mimeType).setCatalog(catalog));
    }

    public boolean delete(String fileKey) {
        return fsProvider.delete(fileKey);
    }

    public String getDownloadUrl(String fileKey) {
        return fsProvider.getDownloadUrl(fileKey);
    }

    public Map<String, Object> createUploadToken(UploadTokenParam param) {
        return fsProvider.createUploadToken(param);
    }

    public FSProvider getProvider() {
        return fsProvider;
    }


}
