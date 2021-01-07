package com.y3tu.tool.report.service;

import com.y3tu.tool.report.entity.domain.Report;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.web.base.jpa.BaseService;

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
     * @param reportDto
     */
    void deleteReport(ReportDto reportDto);
}
