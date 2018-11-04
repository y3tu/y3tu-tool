package com.y3tu.tool.filesystem;


import cn.hutool.core.util.StrUtil;
import com.y3tu.tool.core.util.JsonUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * token参数
 *
 * @author vakin
 */
@Data
public class UploadTokenParam {

    private static final String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String PATH_SEPARATOR = "/";

    /**
     * 过期时间（单位秒）
     */
    private long expires = 3600;
    private String bucketName;
    private String fileType;
    private String uploadDir;
    private String fileName;

    private String callbackUrl;
    private String callbackBody;
    private String callbackHost;
    private boolean callbackBodyUseJson = false;

    /**
     * 如：image/jpg 可以支持通配符image/*
     */
    private String mimeLimit;
    /**
     * 单位 byte
     */
    private Long fsizeMin;
    private Long fsizeMax;

    private Integer deleteAfterDays;

    public void setUploadDir(String uploadDir) {
        this.uploadDir = StrUtil.trimToNull(uploadDir);
        if (uploadDir != null) {
            if (!this.uploadDir.endsWith(PATH_SEPARATOR)) {
                this.uploadDir = this.uploadDir.concat(PATH_SEPARATOR);
            }
            if (this.uploadDir.startsWith(PATH_SEPARATOR)) {
                this.uploadDir = this.uploadDir.substring(1);
            }
        }
    }

    public String getCallbackBodyType() {
        return callbackBodyUseJson ? CONTENT_TYPE_JSON : CONTENT_TYPE_FORM_URLENCODED;
    }

    public void setDeleteAfterDays(Integer deleteAfterDays) {
        if (deleteAfterDays != null && deleteAfterDays > 0) {
            this.deleteAfterDays = deleteAfterDays;
        }
    }

    public String getFileKey() {
        if (StrUtil.isBlank(uploadDir) || StrUtil.isBlank(fileName)) {
            return fileName;
        }
        return uploadDir.concat(fileName);
    }

    public String getCallbackRuleAsJson() {
        if (StrUtil.hasBlank(callbackBody, callbackHost, callbackUrl)) {
            return null;
        }
        Map<String, String> map = new HashMap<>(4);
        map.put("callbackBody", callbackBody);
        map.put("callbackHost", callbackHost);
        map.put("callbackUrl", callbackUrl);
        map.put("callbackBodyType", getCallbackBodyType());
        return JsonUtil.toJson(map);
    }
}
