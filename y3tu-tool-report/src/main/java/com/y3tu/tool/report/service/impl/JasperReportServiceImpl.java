package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.report.entity.domain.ReportAttachment;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.report.entity.dto.ReportParamDto;
import com.y3tu.tool.report.exception.ReportException;
import com.y3tu.tool.report.service.DataSourceService;
import com.y3tu.tool.report.service.JasperReportService;
import com.y3tu.tool.report.service.ReportAttachmentService;
import com.y3tu.tool.report.util.DataSourceUtil;
import com.y3tu.tool.report.util.JasperReportUtil;
import com.y3tu.tool.web.file.service.RemoteFileHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    @Autowired
    RemoteFileHelper remoteFileHelper;
    @Autowired
    ReportAttachmentService reportAttachmentService;

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
    public void exportExcel(ReportDto reportDto, HttpServletResponse response) {
        try {

            Map<String, Object> filePathResult = getJasperTemplate(reportDto.getId());
            String jrxmlFilePath = filePathResult.get("jrxmlFilePath").toString();
            JasperReport jasperReport = JasperReportUtil.getJasperReport(jrxmlFilePath);

            //报参数转换成map
            Map<String, Object> paramMap = new HashMap<>();
            List<ReportParamDto> params = reportDto.getParams();
            for (ReportParamDto paramDto : params) {
                paramMap.put(paramDto.getField(), paramDto.getValue());
            }
            int dsId = reportDto.getDsId();
            javax.sql.DataSource ds = DataSourceUtil.getDataSourceByDsId(dsId);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
            List<Map<String, Object>> data = jdbcTemplate.queryForList(reportDto.getQuerySql());
            JasperPrint jasperPrint = JasperReportUtil.getJasperPrint(jasperReport, paramMap, data);
            JasperReportUtil.exportToExcel(jasperPrint, reportDto.getName(), reportDto.getName(), response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReportException("导出jasper报表失败！" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getJasperTemplate(int reportId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(reportId);
            for (ReportAttachment reportAttachment : reportAttachmentList) {
                //先判断临时文件夹下是否已经有模板文件,如果不存在就从远程服务器中获取到报表template
                String jasperFilePath = FileUtil.SYS_TEM_DIR + FileUtil.getFileNameNoEx(reportAttachment.getTempFileName()) + ".jasper";
                String jrxmlFilePath = FileUtil.SYS_TEM_DIR + reportAttachment.getTempFileName();
                if (!FileUtil.exist(jrxmlFilePath)) {
                    remoteFileHelper.download(reportAttachment.getRemoteFilePath(), jrxmlFilePath);
                }
                JasperCompileManager.compileReportToFile(jrxmlFilePath, jasperFilePath);
                result.put("jasperFilePath", jasperFilePath);
                result.put("jrxmlFilePath", jrxmlFilePath);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

}
