package com.y3tu.tool.filesystem.provider;

/**
 * 提供者枚举
 *
 * @author y3tu
 * @date 2018/10/31
 */
public enum ProviderTypeEnum {
    /**
     * 阿里云oss
     */
    ALIYUN("aliyun"),
    /**
     * 七牛云
     */
    QINIU("qiniu"),
    /**
     * 腾讯云
     */
    TENCENT("tencent"),
    /**
     * fastFDS
     */
    FASTDFS("fastdfs");

    String type;

    ProviderTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
