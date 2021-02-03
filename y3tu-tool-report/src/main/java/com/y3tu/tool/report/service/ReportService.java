package com.y3tu.tool.report.service;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.entity.domain.Report;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.web.base.jpa.BaseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author y3tu
 */
public interface ReportService extends BaseService<Report> {
    /**
     * 创建报表
     *
     * @param reportDto
     */
    void createReport(ReportDto reportDto);

    /**
     * 更新报表
     *
     * @param reportDto
     */
    void updateReport(ReportDto reportDto);

    /**
     * 删除报表
     *
     * @param reportId
     */
    void deleteReport(int reportId);

    /**
     * 下载报表附件
     *
     * @param reportId 报表id
     * @param request
     * @param response
     */
    void download(int reportId, HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取报表html
     *
     * @param reportDto
     * @return
     */
    R reportHtml(ReportDto reportDto,HttpServletResponse response);

    /**
     * 导出报表数据
     *
     * @param reportDto
     * @return
     */
    void exportExcel(ReportDto reportDto, HttpServletResponse response);

}
