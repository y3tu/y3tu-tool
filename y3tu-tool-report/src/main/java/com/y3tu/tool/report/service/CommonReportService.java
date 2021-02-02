package com.y3tu.tool.report.service;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.web.base.jpa.PageInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * 通用报表服务
 *
 * @author y3tu
 */
public interface CommonReportService {
    /**
     * 解析sql
     *
     * @param sql  sql
     * @param dsId 数据源
     * @return
     */
    R parseSql(String sql, int dsId);

    /**
     * 查询报表数据
     *
     * @param reportDto
     * @return
     */
    PageInfo queryReportData(ReportDto reportDto);

    /**
     * 导出报表数据
     *
     * @param reportDto
     * @return
     */
    void export(ReportDto reportDto, HttpServletResponse response);
}
