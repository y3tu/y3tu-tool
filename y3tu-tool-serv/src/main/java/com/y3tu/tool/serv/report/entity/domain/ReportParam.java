package com.y3tu.tool.serv.report.entity.domain;

import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 报表参数配置
 *
 * @author y3tu
 */
@Entity
@Table(name = "report_param")
@Data
public class ReportParam extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(columnDefinition = "int comment '参数序列'")
    Integer seq;

    @Column(name = "report_id", columnDefinition = "int comment '报表ID' ")
    Integer reportId;

    @Column(columnDefinition = "varchar(100) comment '参数名称'")
    String name;

    @Column(columnDefinition = "varchar(100) comment '查询字段'")
    String field;

    @Column(name = "type", columnDefinition = "int comment '取值类型 1:输入取值；2:字典下拉; 3:字典下拉取多值; 4:月份; 5:日期; 6:时间 '")
    Integer type;

    @Column(name = "dict_code", columnDefinition = "varchar(100) comment '字典编码 如果取值类型是字典，则此字段需要填写字典编码'")
    String dictCode;

}
