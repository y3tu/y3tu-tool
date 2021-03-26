package com.y3tu.tool.serv.report.entity.domain;

import com.y3tu.tool.web.annotation.Query;
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
@Table(name = "report_download")
@Data
public class ReportDownload extends BaseEntity {

    public static final String STATUS_WAITING = "00W";
    public static final String STATUS_NORMAL = "00A";
    public static final String STATUS_DISABLE = "00X";
    public static final String STATUS_ERROR = "00E";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * 报表ID
     */
    @Column(name = "report_id", columnDefinition = "int comment '报表ID'")
    @Query(type = Query.Type.EQUAL)
    private Integer reportId;

    @Column(columnDefinition = "varchar(3) comment '报表生成状态 00W:报表生成中 ;00A:报表生成完成，可下载 ;00X:报表下载失效';00E:报表生成失败")
    private String status;

    @Column(name = "download_url", columnDefinition = "varchar(100) comment '报表下载路径'")
    private String downloadUrl;

    @Column(name = "download_times", columnDefinition = "int comment '下载次数'")
    private int downloadTimes;

    @Column(name = "param_json", columnDefinition = "varchar(5000) comment '查询参数json字符串'")
    private String paramJson;

    @Column(name = "err_msg", columnDefinition = "varchar(5000) comment '报表生成失败原因'")
    private String errMsg;
}
