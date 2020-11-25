package com.y3tu.tool.report.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.base.PageInfo;
import com.y3tu.tool.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author y3tu
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/test")
    public R test() {
        PageInfo pageInfo = new PageInfo();
        Map params = new HashMap<>();
        params.put("code","1");
        pageInfo.setParams(params);
        return R.success(reportService.page(pageInfo));
    }
}
