package com.y3tu.tool.report.service;

import com.y3tu.tool.report.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * @author y3tu
 */
public interface ReportService {

    /**
     * 分页查询
     *
     * @param pageable
     * @return
     */
    Page<Report> page(com.y3tu.tool.report.base.Page pageable);
}
