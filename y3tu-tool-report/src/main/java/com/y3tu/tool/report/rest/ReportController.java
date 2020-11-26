package com.y3tu.tool.report.rest;

import com.y3tu.tool.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
