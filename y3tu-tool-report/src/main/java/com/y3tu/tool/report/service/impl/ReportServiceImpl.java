package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.report.entity.domain.Report;
import com.y3tu.tool.report.repository.ReportRepository;
import com.y3tu.tool.report.service.ReportService;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author y3tu
 */
@Service
public class ReportServiceImpl extends BaseServiceImpl<ReportRepository, Report> implements ReportService {

}
