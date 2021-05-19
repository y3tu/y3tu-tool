package com.y3tu.tool.lowcode.generator.entity.domain;

import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 列的数据信息
 *
 * @author y3tu
 */
@Data
@Entity
@Table(name = "column_config")
public class ColumnConfig extends BaseEntity {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * 数据源配置Id
     */
    @Column(name = "ds_id", columnDefinition = "int(10) comment '数据源ID'")
    private int dsId;
    /**
     * 表名
     */
    @Column(name = "table_name", columnDefinition = "varchar(100) comment '表名'")
    private String tableName;

    /**
     * 数据库字段名称
     */
    @Column(name = "column_name", columnDefinition = "varchar(100) comment '数据库字段名称'")
    private String columnName;

    /**
     * 数据库字段类型
     */
    @Column(name = "column_type", columnDefinition = "varchar(100) comment '数据库字段类型'")
    private String columnType;

    /**
     * 是否主键
     */
    @Column(name = "is_key", columnDefinition = "varchar(10) comment '是否主键'")
    private String isKey;

    /**
     * 数据库字段描述
     */
    @Column(columnDefinition = "varchar(500) comment '数据库字段描述'")
    private String remark;

    /**
     * 必填
     */
    @Column(columnDefinition = "varchar(10) comment '是否必填'")
    private String notNull;

    /**
     * 是否在列表显示
     */
    @Column(columnDefinition = "varchar(10) comment '是否在列表显示'")
    private Boolean listShow;

    /**
     * 是否表单显示
     */
    @Column(columnDefinition = "varchar(10) comment '是否表单显示'")
    private Boolean formShow;

    /**
     * 表单类型
     */
    @Column(name = "form_type", columnDefinition = "varchar(10) comment '表单类型'")
    private String formType;

    /**
     * 查询 1:模糊 2：精确
     */
    @Column(name = "query_type", columnDefinition = "varchar(10) comment '查询方式 1:模糊 2：精确'")
    private String queryType;

    /**
     * 字典名称
     */
    @Column(name = "dict_name", columnDefinition = "varchar(100) comment '字典名称'")
    private String dictName;

}
