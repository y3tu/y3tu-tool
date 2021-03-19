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
 * 报表生成日志
 * 当用户点击下载或导出时，如果报表数据量非常大，提示用户到报表生成日志界面等待报表生成完成后再进行下载
 *
 * @author y3tu
 */
@Entity
@Table(name = "generation_log")
@Data
public class GenerationLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * 报表ID
     */
    @Column(name = "report_id", columnDefinition = "int comment '报表ID'")
    private Integer reportId;

    /**
     * 报表生成状态 00W:报表生成中 ;00A:报表生成完成，可下载 ;00X:报表生成失败
     */
    @Column(columnDefinition = "varchar(3) comment '报表生成状态 00W:报表生成中 ;00A:报表生成完成，可下载 ;00X:报表生成失败'")
    private String status;

    /**
     * 报表下载路径
     */
    @Column(name = "download_url", columnDefinition = "varchar(100) comment '报表下载路径'")
    private String downloadUrl;
}
