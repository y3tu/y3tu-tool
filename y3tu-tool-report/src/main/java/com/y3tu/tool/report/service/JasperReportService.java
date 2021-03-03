package com.y3tu.tool.report.service;

import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.web.base.jpa.PageInfo;
import net.sf.jasperreports.engine.JasperReport;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author y3tu
 */
public interface JasperReportService {

    /**
     * 查询报表数据
     *
     * @param reportDto
     * @return
     */
    String reportHtml(ReportDto reportDto, List data, JasperReport jasperReport);

    /**
     * 报表数据导出
     *
     * @param reportDto
     * @param response
     */
    void exportExcel(ReportDto reportDto, HttpServletResponse response);

    /**
     * 获取jasper报表模板
     *
     * @param reportId
     * @return
     */
    Map<String, Object> getJasperTemplate(int reportId);
}
