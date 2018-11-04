package com.y3tu.tool.filesystem.provider.aliyun;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.y3tu.tool.core.date.DateUtil;
import com.y3tu.tool.core.lang.Assert;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.filesystem.UploadObject;
import com.y3tu.tool.filesystem.UploadTokenParam;
import com.y3tu.tool.filesystem.provider.AbstractProvider;
import com.y3tu.tool.setting.Setting;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 阿里oss文件服务
 *
 * @author vakin
 */
public class AliyunProvider extends AbstractProvider {

    public static final String NAME = "aliyun";

    private static final String URL_PREFIX_PATTERN = "(http).*\\.(com|cn)\\/";
    private static final String DEFAULT_CALLBACK_BODY = "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}";

    private OSSClient ossClient;
    private String bucketName;
    private String urlprefix;
    private boolean isPrivate;
    private String accessKeyId;
    private String host;

    public AliyunProvider(String urlprefix, String endpoint, String bucketName, String accessKey, String secretKey, boolean isPrivate) {

        Assert.notBlank(endpoint, "[endpoint] not defined");
        Assert.notBlank(bucketName, "[bucketName] not defined");
        Assert.notBlank(accessKey, "[accessKey] not defined");
        Assert.notBlank(secretKey, "[secretKey] not defined");
        Assert.notBlank(urlprefix, "[urlprefix] not defined");

        this.accessKeyId = accessKey;
        ossClient = new OSSClient(endpoint, accessKey, secretKey);
        this.bucketName = bucketName;
        this.urlprefix = urlprefix.endsWith("/") ? urlprefix : (urlprefix + "/");
        this.isPrivate = isPrivate;
        this.host = StrUtil.removePrefix(urlprefix, "/").split(":")[1];
        if (!ossClient.doesBucketExist(bucketName)) {
            System.out.println("Creating bucket " + bucketName + "\n");
            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(isPrivate ? CannedAccessControlList.Private : CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
        }
    }

    public static AliyunProvider createBySetting(Setting setting) {
        String urlprefix = setting.getStr("urlprefix", "aliyun", "");
        String bucketName = setting.getStr("bucketName", "aliyun", "");
        String accessKey = setting.getStr("accessKey", "aliyun", "");
        String secretKey = setting.getStr("secretKey", "aliyun", "");
        String endpoint = setting.getStr("endpoint", "aliyun", "");
        boolean isPrivate = setting.getBool("private", "aliyun");

        return new AliyunProvider(urlprefix, endpoint, bucketName, accessKey, secretKey, isPrivate);
    }

    @Override
    public String upload(UploadObject object) {
        try {
            PutObjectRequest request = null;
            if (object.getFile() != null) {
                request = new PutObjectRequest(bucketName, object.getFileName(), object.getFile());
            } else if (object.getBytes() != null) {
                request = new PutObjectRequest(bucketName, object.getFileName(), new ByteArrayInputStream(object.getBytes()));
            } else if (object.getInputStream() != null) {
                request = new PutObjectRequest(bucketName, object.getFileName(), object.getInputStream());
            } else {
                throw new IllegalArgumentException("upload object is NULL");
            }

            PutObjectResult result = ossClient.putObject(request);
            if (result.getResponse() == null) {
                return isPrivate ? object.getFileName() : urlprefix + object.getFileName();
            }
            if (result.getResponse().isSuccessful()) {
                return result.getResponse().getUri();
            } else {
                throw new RuntimeException(result.getResponse().getErrorResponseAsString());
            }
        } catch (OSSException e) {
            throw new RuntimeException(e.getErrorMessage());
        }
    }


    //https://help.aliyun.com/document_detail/31926.html
    //https://help.aliyun.com/document_detail/31989.html?spm=a2c4g.11186623.6.907.tlMQcL
    @Override
    public Map<String, Object> createUploadToken(UploadTokenParam param) {

        Map<String, Object> result = new HashMap<>();

        PolicyConditions policyConds = new PolicyConditions();
        if (param.getFsizeMin() != null && param.getFsizeMax() != null) {
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, param.getFsizeMin(), param.getFsizeMax());
        } else {
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        }
        if (param.getUploadDir() != null) {
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, param.getUploadDir());
        }

        if (StrUtil.isBlank(param.getCallbackHost())) {
            param.setCallbackHost(host);
        }

        if (StrUtil.isBlank(param.getCallbackBody())) {
            param.setCallbackBody(DEFAULT_CALLBACK_BODY);
        }

        Date expire = DateUtil.offsetSecond(new Date(), (int) param.getExpires());
        String policy = ossClient.generatePostPolicy(expire, policyConds);
        String policyBase64 = null;
        String callbackBase64 = null;
        try {
            policyBase64 = BinaryUtil.toBase64String(policy.getBytes(StandardCharsets.UTF_8.name()));
            String callbackJson = param.getCallbackRuleAsJson();
            if (callbackJson != null) {
                callbackBase64 = BinaryUtil.toBase64String(callbackJson.getBytes(StandardCharsets.UTF_8.name()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String signature = ossClient.calculatePostSignature(policy);

        result.put("OSSAccessKeyId", accessKeyId);
        result.put("policy", policyBase64);
        result.put("signature", signature);
        result.put("host", this.urlprefix);
        result.put("dir", param.getUploadDir());
        result.put("expire", String.valueOf(expire.getTime()));
        if (callbackBase64 != null) {
            result.put("callback", callbackBase64);
        }
        return result;
    }

    @Override
    public boolean delete(String fileKey) {
        ossClient.deleteObject(bucketName, fileKey);
        return true;
    }

    @Override
    public String getDownloadUrl(String fileKey) {
        //ObjectAcl objectAcl = ossClient.getObjectAcl(bucketName, key);
        if (isPrivate) {
            URL url = ossClient.generatePresignedUrl(bucketName, fileKey, DateUtil.offsetHour(new Date(), 1));
            return url.toString().replaceFirst(URL_PREFIX_PATTERN, urlprefix);
        }
        return urlprefix + fileKey;
    }


    @Override
    public void close() throws IOException {
        ossClient.shutdown();
    }

    @Override
    public String name() {
        return NAME;
    }
}
