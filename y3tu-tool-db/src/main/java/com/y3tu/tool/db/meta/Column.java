package com.y3tu.tool.db.meta;

import com.y3tu.tool.core.text.StringUtils;
import com.y3tu.tool.db.DbRuntimeException;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 数据库表的列信息
 *
 * @author loolly
 */
@Data
public class Column implements Cloneable {

    //----------------------------------------------------- Fields start
    /**
     * 表名
     */
    private String tableName;

    /**
     * 列名
     */
    private String name;
    /**
     * 类型，对应java.sql.Types中的类型
     */
    private int type;
    /**
     * 类型名称 对应java.sql.Types中的类型
     */
    private String typeName;
    /**
     * 大小或数据长度
     */
    private int size;
    /**
     * 是否为可空
     */
    private boolean isNullable;
    /**
     * 注释
     */
    private String comment;
    //----------------------------------------------------- Fields end

    /**
     * 创建列对象
     *
     * @param tableName    表名
     * @param columnMetaRs 列元信息的ResultSet
     * @return 列对象
     */
    public static Column create(String tableName, ResultSet columnMetaRs) {
        return new Column(tableName, columnMetaRs);
    }

    //----------------------------------------------------- Constructor start
    public Column() {
    }

    public Column(String tableName, ResultSet columnMetaRs) {
        try {
            init(tableName, columnMetaRs);
        } catch (SQLException e) {
            throw new DbRuntimeException(StringUtils.format("Get table [{}] meta info error!", tableName));
        }
    }
    //----------------------------------------------------- Constructor end

    /**
     * 初始化
     *
     * @param tableName    表名
     * @param columnMetaRs 列的meta ResultSet
     * @throws SQLException SQL执行异常
     */
    public void init(String tableName, ResultSet columnMetaRs) throws SQLException {
        this.tableName = tableName;

        this.name = columnMetaRs.getString("COLUMN_NAME");
        this.type = columnMetaRs.getInt("DATA_TYPE");
        this.typeName = columnMetaRs.getString("TYPE_NAME");
        this.size = columnMetaRs.getInt("COLUMN_SIZE");
        this.isNullable = columnMetaRs.getBoolean("NULLABLE");
        this.comment = columnMetaRs.getString("REMARKS");
    }

    @Override
    public String toString() {
        return "Column [tableName=" + tableName + ", name=" + name + ", type=" + type + ", size=" + size + ", isNullable=" + isNullable + "]";
    }
}
