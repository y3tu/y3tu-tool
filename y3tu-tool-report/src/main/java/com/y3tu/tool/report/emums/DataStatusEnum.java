package com.y3tu.tool.report.emums;

/**
 * 数据状态枚举
 *
 * @author y3tu
 */
public enum DataStatusEnum {

    NORMAL("00A", "正常"),
    DISABLE("00X", "禁用");


    private String value;

    private String message;

    DataStatusEnum(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
