package com.y3tu.tool.lowcode.common.entity.domain;

import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 报表字典sql数据
 *
 * @author y3tu
 */
@Entity
@Table(name = "report_dict_sql")
@Data
public class DictSql extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "dict_id", columnDefinition = "int comment '字典ID'")
    Integer dictId;

    @Column(name = "ds_id", columnDefinition = "int comment '数据源ID'")
    Integer dsId;

    @Column(columnDefinition = "varchar(3) comment '状态 有效:00A 失效:00X '")
    String status;

    @Column(columnDefinition = "varchar(500) comment '备注'")
    String remarks;

    @Column(name = "query_sql", columnDefinition = "varchar(2000) comment 'sql语句'")
    String querySql;

    @Column(name = "where_column", columnDefinition = "varchar(300) comment '条件字段 多个字段用逗号分隔' ")
    String whereColumn;

}
