package com.y3tu.tool.report.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.entity.domain.Report;
import com.y3tu.tool.report.entity.domain.ReportParam;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.report.entity.dto.ReportParamDto;
import com.y3tu.tool.report.service.ReportParamService;
import com.y3tu.tool.report.service.ReportService;
import com.y3tu.tool.web.base.jpa.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author y3tu
 */
@RestController
@RequestMapping("${y3tu.tool.report.urlPattern:y3tu-tool-report}/report")
public class ReportController {

    @Autowired
    ReportService reportService;
    @Autowired
    ReportParamService reportParamService;

    @PostMapping("page")
    public R page(@RequestBody PageInfo<Report> pageInfo) {
        return R.success(reportService.page(pageInfo));
    }

    @GetMapping("get/{id}")
    public R get(@PathVariable int id) {
        Report report = reportService.getById(id);
        List<ReportParam> params = reportParamService.getByReportId(id);
        ReportDto reportDto = new ReportDto();
        BeanUtils.copyProperties(report, reportDto);
        List<ReportParamDto> paramDtoList = new ArrayList<>();
        for (ReportParam param : params) {
            ReportParamDto paramDto = new ReportParamDto();
            BeanUtils.copyProperties(param, paramDto);
            paramDtoList.add(paramDto);
        }
        reportDto.setParams(paramDtoList);
        return R.success(reportDto);
    }

    @PostMapping("create")
    public R create(@RequestBody ReportDto reportDto) {
        reportService.createReport(reportDto);
        return R.success();
    }

    @PostMapping("update")
    public R update(@RequestBody ReportDto reportDto) {
        reportService.updateReport(reportDto);
        return R.success();
    }

    @GetMapping("delete/{id}")
    public R delete(@PathVariable int id) {
        reportService.delete(id);
        return R.success();
    }
}
