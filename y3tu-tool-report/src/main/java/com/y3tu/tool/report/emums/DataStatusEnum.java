package com.y3tu.tool.report.emums;

/**
 * 数据状态枚举
 */
public enum DataStatusEnum {

    NORMAL(0, "正常"),
    DISABLE(1, "禁用");


    private int value;

    private String message;

    DataStatusEnum(int value, String message) {
        this.value = value;
        this.message = message;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
