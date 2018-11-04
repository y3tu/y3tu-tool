package com.y3tu.tool.web.codegen.entity;

import cn.hutool.core.convert.Convert;

/**
 * 数据库字段类型枚举
 *
 * @author y3tu
 * @date 2018/11/4
 */
public enum DataTypeEnum {
    /**
     * 字段类型
     */
    INT(1, "Integer"),
    VARCHAR(2, "String"),
    TIMESTAMP(3, "Date"),
    CHAR(4, "String"),
    DATE(5, "Date");

    private int dataType;
    private String javaType;

    DataTypeEnum(int dataType, String javaType) {
        this.dataType = dataType;
        this.javaType = javaType;
    }

    public static String getJavaType(int dataType) {
        for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()) {
            if (dataTypeEnum.dataType == dataType) {
                return dataTypeEnum.javaType;
            }
        }
        return Convert.toStr(dataType);
    }
}
