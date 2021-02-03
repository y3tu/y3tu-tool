package com.y3tu.tool.report.rest;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.entity.domain.Report;
import com.y3tu.tool.report.entity.domain.ReportAttachment;
import com.y3tu.tool.report.entity.domain.ReportParam;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.report.entity.dto.ReportParamDto;
import com.y3tu.tool.report.service.CommonReportService;
import com.y3tu.tool.report.service.ReportAttachmentService;
import com.y3tu.tool.report.service.ReportParamService;
import com.y3tu.tool.report.service.ReportService;
import com.y3tu.tool.web.base.jpa.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    ReportAttachmentService reportAttachmentService;
    @Autowired
    CommonReportService commonReportService;

    @PostMapping("page")
    public R page(@RequestBody PageInfo<Report> pageInfo) {
        return R.success(reportService.page(pageInfo));
    }

    @GetMapping("get/{id}")
    public R get(@PathVariable int id) {
        Report report = reportService.getById(id);
        return R.success(buildReportDto(report));
    }

    @GetMapping("getByName/{name}")
    public R getByName(@PathVariable String name) {
        Report report = reportService.getByName(name);
        return R.success(buildReportDto(report));
    }

    private ReportDto buildReportDto(Report report) {
        List<ReportParam> params = reportParamService.getByReportId(report.getId());
        ReportDto reportDto = new ReportDto();
        BeanUtils.copyProperties(report, reportDto);
        //报表参数
        List<ReportParamDto> paramDtoList = new ArrayList<>();
        for (ReportParam param : params) {
            ReportParamDto paramDto = new ReportParamDto();
            BeanUtils.copyProperties(param, paramDto);
            paramDtoList.add(paramDto);
        }
        reportDto.setParams(paramDtoList);
        //报表附件信息
        List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(report.getId());
        for (ReportAttachment reportAttachment : reportAttachmentList) {
            reportDto.setFileName(reportAttachment.getName());
        }
        return reportDto;
    }

    @PostMapping("create")
    public R create(@RequestBody ReportDto reportDto) {
        reportService.createReport(reportDto);
        return R.success();
    }

    @PostMapping("upload")
    public R upload(@RequestParam("fileTempPrefix") String fileTempPrefix, @RequestParam("file") MultipartFile file) {
        reportAttachmentService.upload(fileTempPrefix, file);
        return R.success();
    }

    @PostMapping("update")
    public R update(@RequestBody ReportDto reportDto) {
        reportService.updateReport(reportDto);
        return R.success();
    }

    @GetMapping("delete/{id}")
    public R delete(@PathVariable int id) {
        reportService.deleteReport(id);
        return R.success();
    }

    /**
     * 下载报表附件
     *
     * @param id
     * @param request
     * @param response
     */
    @GetMapping("download/{id}")
    public void download(@PathVariable int id, HttpServletRequest request, HttpServletResponse response) {
        reportService.download(id, request, response);
    }

    /**
     * 解析SQL获取表头信息
     *
     * @param reportDto
     * @return
     */
    @PostMapping("parseSqlForHeader")
    public R parseSqlForHeader(@RequestBody ReportDto reportDto) {
        return commonReportService.parseSqlForHeader(reportDto.getQuerySql(), reportDto.getDsId());
    }

    /**
     * 获取报表html
     *
     * @param reportDto
     * @param response
     * @return
     */
    @PostMapping("reportHtml")
    public R reportHtml(@RequestBody ReportDto reportDto, HttpServletResponse response) {
        return reportService.reportHtml(reportDto, response);
    }

    /**
     * 导出报表excel数据
     *
     * @return
     */
    @PostMapping("exportExcel")
    public void exportExcel(@RequestBody ReportDto reportDto, HttpServletResponse response) {
        reportService.exportExcel(reportDto, response);
    }
}
