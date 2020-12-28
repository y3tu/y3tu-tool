package com.y3tu.tool.report.domain;

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
public class ReportParam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "int comment '参数序列'")
    int seq;

    @Column(name = "report_id", columnDefinition = "int comment '报表ID' ")
    long reportId;

    @Column(name = "param_name", columnDefinition = "varchar(100) comment '参数名称'")
    String paramName;

    @Column(name = "query_name", columnDefinition = "varchar(100) comment '查询参数'")
    String queryParam;

    @Column(columnDefinition = "int comment '关联关系 1:等于,2:不等于,3:大于,4:小于,5:大于等于,6:小于等于,7:in,8:like,9:not in,10: not like'")
    String relation;

    @Column(name = "value_type", columnDefinition = "int comment '取值类型 1. 输入取值；2. 账期选择; 3. 账期天选择; 4. 字典下拉; 5. 字典下拉取多值; 6.sql下拉; 7. sql下拉取多值；8.多sheet页参数:字典下拉;9.多sheet页参数:sql下拉;10:特殊参数；11:输入取值(默认值); 12:输入取值(默认值展示)'")
    String valueType;

    @Column(name = "dict_code", columnDefinition = "varchar(100) comment '字典编码 如果取值类型是字典，则此字段需要填写字典编码'")
    String dictCode;

}
