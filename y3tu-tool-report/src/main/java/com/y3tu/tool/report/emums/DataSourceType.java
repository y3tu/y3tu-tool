package com.y3tu.tool.report.emums;

/**
 * 数据源类型枚举
 *
 * @author y3tu
 */
public enum DataSourceType {
    /**
     * mysql
     */
    MYSQL(0),
    /**
     * oracle
     */
    ORACLE(1);

    private int value;

    private DataSourceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
