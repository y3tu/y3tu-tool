package com.y3tu.tool.report.domain;

import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 报表数据源
 *
 * @author y3tu
 */
@Entity
@Table(name = "report_data_source")
@Data
public class ReportDataSource extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "varchar(100) COMMENT '数据源名称'")
    String name;

    @Column(columnDefinition = "varchar(100) COMMENT '编码'")
    String code;

    @Column(columnDefinition = "varchar(200) COMMENT '备注'")
    String remark;

    @Column(name = "db_type", columnDefinition = "varchar(10) COMMENT '数据库类型'")
    String dbType;

    @Column(name = "db_driver", columnDefinition = "varchar(100) COMMENT '驱动类'")
    String dbDriver;

    @Column(name = "db_url", columnDefinition = "varchar(500) COMMENT '数据源地址'")
    String dbUrl;

    @Column(name = "db_username", columnDefinition = "varchar(100) COMMENT '用户名'")
    String dbUsername;

    @Column(name = "db_password", columnDefinition = "varchar(100) COMMENT '密码'")
    String dbPassword;
}
