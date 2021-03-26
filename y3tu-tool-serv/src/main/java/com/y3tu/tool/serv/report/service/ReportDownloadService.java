package com.y3tu.tool.serv.report.service;

import com.y3tu.tool.serv.report.entity.domain.ReportDownload;
import com.y3tu.tool.web.base.jpa.BaseService;

import java.util.List;

/**
 * @author y3tu
 */
public interface ReportDownloadService extends BaseService<ReportDownload> {
    /**
     * 根据reportId获取有效的和正在生成的报表下载记录
     * @param reportId
     * @return
     */
    List<ReportDownload> getByReportId(int reportId);

}
