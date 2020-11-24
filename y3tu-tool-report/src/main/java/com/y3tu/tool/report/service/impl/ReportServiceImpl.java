package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.report.domain.Report;
import com.y3tu.tool.report.repository.ReportRepository;
import com.y3tu.tool.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * @author y3tu
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportRepository reportRepository;

    @Override
    public Page<Report> page(com.y3tu.tool.report.base.Page pageable) {
        Page<Report> page = reportRepository.findAll(pageable);
        return page;
    }
}
