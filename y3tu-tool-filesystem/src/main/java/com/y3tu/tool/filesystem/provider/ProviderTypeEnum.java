package com.y3tu.tool.filesystem.provider;

/**
 * 提供者枚举
 *
 * @author y3tu
 * @date 2018/10/31
 */
public enum ProviderTypeEnum {
    /**
     * 提供者枚举
     */
    ALIYUN("aliyun"),
    QINIU("qiniu");

    String type;

    ProviderTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
