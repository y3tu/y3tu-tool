package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.report.entity.dto.ReportParamDto;
import com.y3tu.tool.report.exception.ReportException;
import com.y3tu.tool.report.service.DataSourceService;
import com.y3tu.tool.report.service.JasperReportService;
import com.y3tu.tool.report.util.DataSourceUtil;
import com.y3tu.tool.report.util.JasperReportUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author y3tu
 */
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Service
public class JasperReportServiceImpl implements JasperReportService {

    @Autowired
    DataSourceService dataSourceService;

    @Override
    public String reportHtml(ReportDto reportDto, List data, JasperReport jasperReport) {
        try {
            //报参数转换成map
            Map<String, Object> paramMap = new HashMap<>();
            List<ReportParamDto> params = reportDto.getParams();
            for (ReportParamDto paramDto : params) {
                paramMap.put(paramDto.getField(), paramDto.getValue());
            }
            JasperPrint jasperPrint = JasperReportUtil.getJasperPrint(jasperReport, paramMap, data);
            return JasperReportUtil.exportToHtml(jasperPrint);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReportException("查询jasper报表数据失败！" + e.getMessage());
        }
    }

    @Override
    public void exportExcel(ReportDto reportDto, String jrxmlFilePath, HttpServletResponse response) {

        try {
            //报参数转换成map
            Map<String, Object> paramMap = new HashMap<>();
            List<ReportParamDto> params = reportDto.getParams();
            for (ReportParamDto paramDto : params) {
                paramMap.put(paramDto.getField(), paramDto.getValue());
            }
            //获取模板中的sql查询语句
            JasperReport jasperReport = JasperReportUtil.getJasperReport(jrxmlFilePath);

            int dsId = reportDto.getDsId();
            javax.sql.DataSource ds = DataSourceUtil.getDataSourceByDsId(dsId);
            JasperPrint jasperPrint = JasperReportUtil.getJasperPrint(jasperReport, paramMap, ds.getConnection());
            JasperReportUtil.exportToExcel(jasperPrint, reportDto.getName(), reportDto.getName(), response);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReportException("导出jasper报表失败！" + e.getMessage());
        }
    }


}
