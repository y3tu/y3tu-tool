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
 * 报表附件信息
 *
 * @author y3tu
 */
@Entity
@Table(name = "report_attachment")
@Data
public class ReportAttachment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "report_id", columnDefinition = "int comment '报表ID'")
    Integer reportId;

    @Column(columnDefinition = "varchar(100) comment '附件名称'")
    String name;

    @Column(name = "real_file_name", columnDefinition = "varchar(100) comment '附件保存在远程服务器上的真实名称'")
    String realFileName;

    @Column(name = "remote_file_path", columnDefinition = "varchar(200) comment '附件保存在远程服务器上的全路径'")
    String remoteFilePath;

    @Column(columnDefinition = "int comment '附件类型:1:数据模板;2:柱状图模板;3:折线图模板;4:饼图模板'")
    Integer type;

    @Column(columnDefinition = "varchar(3) comment '状态：00A:有效;00X:失效'")
    String status;
}
