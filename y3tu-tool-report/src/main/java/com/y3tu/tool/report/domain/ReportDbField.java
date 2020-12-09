package com.y3tu.tool.report.domain;

import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author y3tu
 */
@Entity
@Table(name = "report_db_field")
@Data
public class ReportDbField extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "field_name",columnDefinition = "varchar(80) COMMENT '字段名'")
    private String fieldName;

    @Column(name = "field_text",columnDefinition = "varchar(80) COMMENT '字段文本'")
    private String fieldText;
}
