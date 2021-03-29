package com.y3tu.tool.server.report.service;

import com.y3tu.tool.server.report.entity.dto.ReportDto;
import net.sf.jasperreports.engine.JasperReport;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
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
     * 报表数据导出到流
     *
     * @param reportDto
     * @param outputStream
     */
    void exportExcel(ReportDto reportDto, OutputStream outputStream);

    /**
     * 获取jasper报表模板
     *
     * @param reportId
     * @return
     */
    Map<String, Object> getJasperTemplate(int reportId);
}
