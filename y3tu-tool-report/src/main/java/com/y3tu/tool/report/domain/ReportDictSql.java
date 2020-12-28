package com.y3tu.tool.report.domain;

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
public class ReportDictSql {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "dict_id", columnDefinition = "int comment '字典ID'")
    long dictId;

    @Column(name = "ds_id", columnDefinition = "int comment '数据源ID'")
    long dsId;

    @Column(columnDefinition = "varchar(3) comment '状态'")
    String status;

    @Column(columnDefinition = "varchar(500) comment '备注'")
    String remarks;

    @Column(name = "query_sql", columnDefinition = "varchar(2000) comment 'sql语句'")
    String querySql;

}
