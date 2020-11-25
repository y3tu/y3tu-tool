package com.y3tu.tool.report.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.repository.ReportRepository;
import com.y3tu.tool.report.service.ReportService;
import com.y3tu.tool.web.sql.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author y3tu
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportRepository reportRepository;

    @GetMapping("/test")
    public R test() {
        int count = SqlUtil.count("select * from report", "support");
        return R.success(reportRepository.findById(1));
    }
}
