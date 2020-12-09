package com.y3tu.tool.report.domain;

import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 报表查询参数
 *
 * @author y3tu
 */
@Entity
@Table(name = "report_db_param")
@Data
public class ReportDbParam extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "report_id", columnDefinition = "int(10) COMMENT '字段名'")
    private long reportId;

    @Column(name = "param_name", columnDefinition = "varchar(32) COMMENT '参数字段'")
    private String paramName;

    @Column(name = "param_txt", columnDefinition = "varchar(32) COMMENT '参数文本'")
    private String paramTxt;

    @Column(name = "param_value", columnDefinition = "varchar(32) COMMENT '参数默认值'")
    private String paramValue;
}
