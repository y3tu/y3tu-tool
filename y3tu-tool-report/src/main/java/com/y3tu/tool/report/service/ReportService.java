package com.y3tu.tool.report.service;

import com.y3tu.tool.report.base.PageInfo;
import com.y3tu.tool.report.domain.Report;


/**
 * @author y3tu
 */
public interface ReportService {

    /**
     * 分页查询
     *
     * @param
     * @return
     */
    PageInfo<Report> page(PageInfo page);
}
