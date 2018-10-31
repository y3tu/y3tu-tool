package com.y3tu.tool.filesystem.provider.tencent;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.y3tu.tool.core.date.DateUtil;
import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.lang.Assert;
import com.y3tu.tool.core.thread.ThreadUtil;
import com.y3tu.tool.filesystem.UploadObject;
import com.y3tu.tool.filesystem.UploadTokenParam;
import com.y3tu.tool.filesystem.provider.AbstractProvider;
import com.y3tu.tool.setting.Setting;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 腾讯云文件服务cos
 *
 * @author y3tu
 * @date 2018/10/31
 */
public class TencentProvider extends AbstractProvider {

    public static final String NAME = "tencent";

    /**
     * 桶的名称
     */
    String bucketName = "bj-demo";
    /**
     * 区域 北京则beijing
     */
    String region = "ap-beijing";
    /**
     * accessKey 是用于标识 API 调用者的身份
     */
    String accessKey;
    /**
     * secretKey是用于加密签名字符串和服务器端验证签名字符串的密钥
     */
    String secretKey;

    COSCredentials cred = null;
    TransferManager transferManager = null;
    COSClient cosClient = null;

    public TencentProvider(String bucketName, String accessKey, String secretKey, String region) {
        Assert.notBlank(bucketName, "[bucketName] not defined");
        Assert.notBlank(accessKey, "[accessKey] not defined");
        Assert.notBlank(secretKey, "[secretKey] not defined");
        Assert.notBlank(region, "[region] not defined");

        this.bucketName = bucketName;
        this.region = region;
        // 1 初始化用户身份信息(accessKey, secretKey)
        cred = new BasicCOSCredentials(accessKey, secretKey);
        // 2 设置bucket的区域,
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 3 生成cos客户端
        cosClient = new COSClient(cred, clientConfig);
        // 指定要上传到 COS 上的路径
        ExecutorService threadPool = ThreadUtil.newExecutor();
        // 传入一个 threadpool, 若不传入线程池, 默认 TransferManager 中会生成一个单线程的线程池。
        transferManager = new TransferManager(cosClient, threadPool);

    }

    public static TencentProvider createBySetting(Setting setting) {
        String bucketName = setting.getStr("bucketName", "tencent", "");
        String accessKey = setting.getStr("accessKey", "tencent", "");
        String secretKey = setting.getStr("secretKey", "tencent", "");
        String region = setting.getStr("region", "tencent", "");
        return new TencentProvider(bucketName, accessKey, secretKey, region);
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public String upload(UploadObject object) {
        try {
            // bucket 的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            String bucket = bucketName;
            File localFile = FileUtil.file(object.getFile());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, object.getFileName(), localFile);
            // 本地文件上传
            Upload upload = transferManager.upload(putObjectRequest);
            // 异步（如果想等待传输结束，则调用 waitForUploadResult）
            // UploadResult uploadResult = upload.waitForUploadResult();
            // 同步的等待上传结束waitForCompletion
            upload.waitForCompletion();
            //获取上传成功之后文件的下载地址
            URL url = cosClient.generatePresignedUrl(bucketName, object.getFileName(), new Date(DateUtil.date().getTime() + 5 * 60 * 10000));
            return url.getPath();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭 TransferManger
            transferManager.shutdownNow();
        }
        return null;
    }

    @Override
    public String downloadAndSaveAs(String file, String localSaveDir) {
        try {
            //下载到本地指定路径
            File localDownFile = new File(localSaveDir);
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, file);
            // 下载文件
            Download download = transferManager.download(getObjectRequest, localDownFile);
            // 等待传输结束（如果想同步的等待上传结束，则调用 waitForCompletion）
            download.waitForCompletion();
            System.out.println("下载成功");
        } catch (Throwable tb) {
            System.out.println("下载失败");
            tb.printStackTrace();
        } finally {
            // 关闭 TransferManger
            transferManager.shutdownNow();
        }
        return localSaveDir;
    }

    @Override
    public String getDownloadUrl(String fileKey) {
        return null;
    }


    @Override
    public boolean delete(String fileKey) {
        try {
            cosClient.deleteObject(bucketName, fileKey);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> createUploadToken(UploadTokenParam param) {
        return null;
    }

    @Override
    public void close() throws IOException {
        cosClient.shutdown();
    }
}
