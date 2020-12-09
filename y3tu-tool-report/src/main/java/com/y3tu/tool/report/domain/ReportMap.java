package com.y3tu.tool.report.domain;

import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 报表地图
 *
 * @author y3tu
 */
@Entity
@Table(name = "report_map")
@Data
public class ReportMap extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "varchar(100) COMMENT '地图名称'")
    private String name;

    @Column(columnDefinition = "varchar(100) COMMENT '地图编码'", unique = true)
    private String code;

    @Column(columnDefinition = "varchar(5000) COMMENT '地图数据'", unique = true)
    private String data;
}
