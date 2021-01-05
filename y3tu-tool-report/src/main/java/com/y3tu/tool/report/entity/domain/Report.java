package com.y3tu.tool.report.entity.domain;

import com.y3tu.tool.web.annotation.Query;
import com.y3tu.tool.web.base.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * 报表
 *
 * @author y3tu
 */
@Entity
@Table(name = "report")
@Data
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(columnDefinition = "varchar(50) comment '名称'")
    @Query(type = Query.Type.INNER_LIKE)
    String name;

    @Column(columnDefinition = "varchar(1000) comment '备注'")
    String remarks;

    @Column(columnDefinition = "varchar(10) comment '状态 0:正常 1：禁用'")
    Integer status;

    @Column(columnDefinition = "int comment '类型 1:通用报表 2:个性化报表 '")
    Integer type;

    @Column(name = "ds_id", columnDefinition = "int(10) comment '数据源ID'")
    Integer dsId;

    @Column(name = "column_header", columnDefinition = "varchar(500) comment '列表头'")
    private String columnHeader;

    @Column(name = "query_sql", columnDefinition = "varchar(2000) comment '报表查询sql'")
    String querySql;

    @Column(name = "view_count", columnDefinition = "int(15) default 0 comment '浏览次数' ")
    Integer viewCount;


    /**
     * 数据库中不存在此字段用@Transient注解
     */
    @Transient
    String test;
}
