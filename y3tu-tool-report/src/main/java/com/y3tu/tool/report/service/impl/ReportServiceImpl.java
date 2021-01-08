package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.report.entity.domain.Report;
import com.y3tu.tool.report.entity.domain.ReportParam;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.report.repository.ReportRepository;
import com.y3tu.tool.report.service.ReportParamService;
import com.y3tu.tool.report.service.ReportService;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author y3tu
 */
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Service
public class ReportServiceImpl extends BaseServiceImpl<ReportRepository, Report> implements ReportService {


    @Autowired
    ReportParamService reportParamService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createReport(ReportDto reportDto) {
        //报表内容
        Report report = new Report();
        BeanUtils.copyProperties(reportDto, report);
        report.setCreateTime(new Date());
        report = this.create(report);
        //报表参数
        createReportParam(reportDto, report.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReport(ReportDto reportDto) {
        //报表内容
        Report report = new Report();
        BeanUtils.copyProperties(reportDto, report);
        report.setUpdateTime(new Date());
        this.update(report);
        //报表参数 先删除旧参数数据再新增
        reportParamService.deleteByReportId(reportDto.getId());
        createReportParam(reportDto, reportDto.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReport(ReportDto reportDto) {
        //先删除报表参数再删除报表
        reportParamService.deleteByReportId(reportDto.getId());
        this.delete(reportDto.getId());
    }

    private void createReportParam(ReportDto reportDto, int reportId) {
        for (int i = 0; i < reportDto.getParams().size(); i++) {
            ReportParam param = new ReportParam();
            BeanUtils.copyProperties(reportDto.getParams().get(i), param);
            param.setReportId(reportId);
            param.setSeq(i);
            param.setCreateTime(new Date());
            reportParamService.create(param);
        }
    }
}
