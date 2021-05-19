package com.y3tu.tool.lowcode.report.service;

import com.y3tu.tool.lowcode.report.entity.domain.ReportDownload;
import com.y3tu.tool.web.base.jpa.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 获取所有待处理的报表下载
     * @return
     */
    List<ReportDownload> getWaitData();

    /**
     * 处理报表下载并生成文件
     * @param reportDownload
     */
    void handleDownload(ReportDownload reportDownload);

    /**
     * 下载报表
     *
     * @param id
     * @param request
     * @param response
     */
    void download(int id, HttpServletRequest request, HttpServletResponse response);

}
