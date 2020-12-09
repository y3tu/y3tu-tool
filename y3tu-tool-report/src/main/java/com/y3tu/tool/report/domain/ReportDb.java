package com.y3tu.tool.report.domain;

import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author y3tu
 */
@Entity
@Table(name = "report_db")
@Data
public class ReportDb extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "report_id",columnDefinition = "int(10) COMMENT '报表ID'")
    private long reportId;



}
