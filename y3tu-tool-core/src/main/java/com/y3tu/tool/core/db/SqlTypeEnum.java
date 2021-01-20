package com.y3tu.tool.core.db;

/**
 * 数据库类型
 *
 * @author y3tu
 */
public enum SqlTypeEnum {
    MYSQL("mysql"),

    ORACLE("oracle");

    private String type;

    SqlTypeEnum(String type) {
        this.type = type;
    }
}
