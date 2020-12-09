package com.y3tu.tool.report.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.domain.Report;
import com.y3tu.tool.report.service.ReportService;
import com.y3tu.tool.web.base.jpa.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author y3tu
 */
@RestController
@RequestMapping("report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @PostMapping("page")
    public R page(@RequestBody PageInfo<Report> pageInfo) {
        return R.success(reportService.page(pageInfo));
    }

    @GetMapping("get/{id}")
    public R get(@PathVariable long id) {
        return R.success(reportService.findById(id));
    }



}
