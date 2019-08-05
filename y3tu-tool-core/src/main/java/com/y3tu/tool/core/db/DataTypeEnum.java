package com.y3tu.tool.core.db;

/**
 * 数据库字段类型枚举
 *
 * @author y3tu
 * @date 2018/11/5
 */
public enum DataTypeEnum {
    /**
     * 字段类型
     */
    INT("INT", "Integer"),
    VARCHAR("VARCHAR", "String"),
    TIMESTAMP("TIMESTAMP", "Date"),
    CHAR("CHAR", "String"),
    DATE("DATE", "Date"),
    DATETIME("DATETIME","Date"),
    BIT("BIT","Integer"),
    DECIMAL("DECIMAL","BigDecimal");

    private String dataType;
    private String javaType;

    DataTypeEnum(String dataType, String javaType) {
        this.dataType = dataType;
        this.javaType = javaType;
    }

    public static String getJavaType(String dataType) {
        for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()) {
            if (dataTypeEnum.dataType.equals(dataType)) {
                return dataTypeEnum.javaType;
            }
        }
        return dataType;
    }
}
