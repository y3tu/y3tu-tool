package com.y3tu.tool.serv.report.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author y3tu
 */
@Data
public class ReportDownloadDto implements Serializable {

    private Integer id;

    private Integer reportId;

    private String reportName;

    private String status;

    private String downloadUrl;

    private int downloadTimes;

    private String paramJson;

    private String errMsg;
}
