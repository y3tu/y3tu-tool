package com.y3tu.tool.report.domain;

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
    private long id;

    @Column(columnDefinition = "varchar(50) COMMENT '编码'", unique = true)
    String code;

    @Column(columnDefinition = "varchar(50) comment '名称'")
    String name;

    @Column(columnDefinition = "varchar(10) comment '状态'")
    String status;

    @Column(columnDefinition = "varchar(10) comment '类型'")
    String type;

    @Column(columnDefinition = "varchar(4000) comment 'json字符串'")
    String jsonStr;

    @Column(columnDefinition = "varchar(4000) comment '缩略图'")
    String thumb;

    @Column(name = "view_count", columnDefinition = "int(15) default 0 comment '浏览次数' ")
    int viewCount;

    /**
     * 数据库中不存在此字段用@Transient注解
     */
    @Transient
    String test;
}
