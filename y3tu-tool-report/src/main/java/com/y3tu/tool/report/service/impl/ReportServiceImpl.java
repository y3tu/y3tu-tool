package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.report.base.PageInfo;
import com.y3tu.tool.report.domain.Report;
import com.y3tu.tool.report.repository.ReportRepository;
import com.y3tu.tool.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * @author y3tu
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportRepository reportRepository;

    @Override
    public PageInfo<Report> page(PageInfo page) {
        Pageable pageable = PageRequest.of(page.getCurrent(), page.getSize());
        reportRepository.findAll();
        Page<Report> page1 = reportRepository.page(page.getParams(), pageable);
        System.out.println(page1);
        return null;
    }
}
