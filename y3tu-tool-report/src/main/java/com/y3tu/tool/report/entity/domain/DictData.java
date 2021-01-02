package com.y3tu.tool.report.entity.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 报表字典数据
 *
 * @author y3tu
 */
@Entity
@Table(name = "report_dict_data")
@Data
public class DictData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "dict_id", columnDefinition = "int comment '字典ID'")
    long dictId;

    @Column(columnDefinition = "varchar(100) comment '字典数据名称'")
    String name;

    @Column(columnDefinition = "varchar(100) comment '字典值'")
    String value;

    @Column(columnDefinition = "varchar(3) comment '状态'")
    int status;

    @Column(columnDefinition = "int comment '排序'")
    int seq;

    @Column(columnDefinition = "varchar(500) comment '备注'")
    String remarks;
}
