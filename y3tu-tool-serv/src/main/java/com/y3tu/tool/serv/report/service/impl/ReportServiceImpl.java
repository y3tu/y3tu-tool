package com.y3tu.tool.serv.report.service.impl;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.serv.report.configure.ToolReportProperties;
import com.y3tu.tool.serv.report.entity.domain.Report;
import com.y3tu.tool.serv.report.entity.domain.ReportAttachment;
import com.y3tu.tool.serv.report.entity.domain.ReportParam;
import com.y3tu.tool.serv.report.entity.dto.ReportDto;
import com.y3tu.tool.serv.report.entity.dto.ReportParamDto;
import com.y3tu.tool.serv.report.exception.ReportException;
import com.y3tu.tool.serv.report.repository.ReportRepository;
import com.y3tu.tool.serv.report.service.CommonReportService;
import com.y3tu.tool.serv.common.service.DataSourceService;
import com.y3tu.tool.serv.report.service.JasperReportService;
import com.y3tu.tool.serv.report.service.ReportAttachmentService;
import com.y3tu.tool.serv.report.service.ReportParamService;
import com.y3tu.tool.serv.report.service.ReportService;
import com.y3tu.tool.serv.common.util.DataSourceUtil;
import com.y3tu.tool.serv.report.util.JasperReportUtil;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import com.y3tu.tool.web.base.jpa.PageInfo;
import com.y3tu.tool.web.file.service.RemoteFileHelper;
import com.y3tu.tool.web.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author y3tu
 */
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Service
public class ReportServiceImpl extends BaseServiceImpl<ReportRepository, Report> implements ReportService {

    @Autowired
    ReportParamService reportParamService;
    @Autowired
    ReportAttachmentService reportAttachmentService;
    @Autowired
    CommonReportService commonReportService;
    @Autowired
    JasperReportService jasperReportService;
    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    RemoteFileHelper remoteFileHelper;
    @Autowired
    ToolReportProperties properties;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createReport(ReportDto reportDto) {
        //报表内容
        Report report = new Report();
        BeanUtils.copyProperties(reportDto, report);
        report.setCreateTime(new Date());
        report = this.create(report);
        //处理报表参数
        createReportParam(reportDto, report.getId());
        //处理报表模板附件
        if (StrUtil.isNotEmpty(reportDto.getFileName())) {
            //附件上传到远程服务器
            String tempFileName = reportDto.getFileTempPrefix() + ".jrxml";
            String tempFilePath = FileUtil.SYS_TEM_DIR + tempFileName;
            boolean flag = remoteFileHelper.upload(properties.getRemotePath(), tempFileName, tempFilePath);
            //删除临时目录文件
            FileUtil.del(tempFilePath);
            if (flag) {
                //文件上传成功
                ReportAttachment reportAttachment = new ReportAttachment();
                reportAttachment.setReportId(report.getId());
                reportAttachment.setStatus("00A");
                reportAttachment.setName(reportDto.getFileName());
                reportAttachment.setTempFileName(tempFileName);
                reportAttachment.setRemoteFilePath(properties.getRemotePath() + tempFileName);
                reportAttachment.setCreateTime(new Date());
                reportAttachmentService.create(reportAttachment);
            } else {
                throw new ReportException("文件上传失败！请重新添加报表模板");
            }
        }
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
        //处理报表模板附件 更新时有可能不会上传模板，需要判断FileTempPrefix是否有值
        if (StrUtil.isNotEmpty(reportDto.getFileName())
                && StrUtil.isNotEmpty(reportDto.getFileTempPrefix())) {
            //附件上传到远程服务器
            String tempFileName = reportDto.getFileTempPrefix() + ".jrxml";
            String tempFilePath = FileUtil.SYS_TEM_DIR + tempFileName;
            boolean flag = remoteFileHelper.upload(properties.getRemotePath(), tempFileName, tempFilePath);
            //删除临时目录文件
            FileUtil.del(tempFilePath);
            if (flag) {
                //文件上传成功 更新记录
                List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(report.getId());
                if (reportAttachmentList.isEmpty()) {
                    //如果没有附件记录就新增
                    ReportAttachment reportAttachment = new ReportAttachment();
                    reportAttachment.setReportId(report.getId());
                    reportAttachment.setStatus("00A");
                    reportAttachment.setName(reportDto.getFileName());
                    reportAttachment.setTempFileName(tempFileName);
                    reportAttachment.setRemoteFilePath(properties.getRemotePath() + tempFileName);
                    reportAttachment.setCreateTime(new Date());
                    reportAttachmentService.create(reportAttachment);
                } else {
                    for (ReportAttachment reportAttachment : reportAttachmentList) {
                        String oldPath = reportAttachment.getRemoteFilePath();
                        reportAttachment.setName(reportDto.getFileName());
                        reportAttachment.setTempFileName(tempFileName);
                        reportAttachment.setRemoteFilePath(properties.getRemotePath() + tempFileName);
                        reportAttachment.setUpdateTime(new Date());
                        reportAttachmentService.update(reportAttachment);
                        //删除老附件
                        if (!remoteFileHelper.remove(oldPath)) {
                            throw new ReportException("删除远程服务器上的附件失败！");
                        }
                    }
                }
            } else {
                throw new ReportException("文件上传失败！请重新添加报表模板");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReport(int reportId) {
        //先删除报表参数再删除报表
        reportParamService.deleteByReportId(reportId);
        this.delete(reportId);
        //最后删除附件记录和远程服务器上的附件
        List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(reportId);
        reportAttachmentService.deleteByReportId(reportId);
        for (ReportAttachment reportAttachment : reportAttachmentList) {
            if (!remoteFileHelper.remove(reportAttachment.getRemoteFilePath())) {
                throw new ReportException("删除远程服务器上的附件失败！");
            }
        }
    }

    @Override
    public Report getByName(String name) {
        return this.repository.getByName(name);
    }

    @Override
    public void download(int reportId, HttpServletRequest request, HttpServletResponse response) {
        List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(reportId);
        for (ReportAttachment reportAttachment : reportAttachmentList) {
            remoteFileHelper.download(reportAttachment.getRemoteFilePath(), reportAttachment.getName(), false, request, response);
        }
    }

    @Override
    public R reportHtml(ReportDto reportDto, HttpServletResponse response) {
        try {
            if (Report.TYPE_COMMON.equals(reportDto.getType())) {
                //通用报表
                //替换sql参数
                reportDto.setQuerySql(replaceParamSql(reportDto.getQuerySql(), reportDto.getParams()));
                PageInfo pageInfo = commonReportService.reportHtml(reportDto);
                return R.success(pageInfo);
            } else if (Report.TYPE_JASPER.equals(reportDto.getType())) {
                //Jasper报表
                //获取jasper模板文件地址
                Map<String, Object> filePathResult = jasperReportService.getJasperTemplate(reportDto.getId());
                String jrxmlFilePath = filePathResult.get("jrxmlFilePath").toString();
                JasperReport jasperReport = JasperReportUtil.getJasperReport(jrxmlFilePath);
                String querySql = jasperReport.getQuery().getText();
                String html = "";
                PageInfo pageInfo = null;
                if (StrUtil.isNotEmpty(querySql)) {
                    reportDto.setQuerySql(replaceParamSql(querySql, reportDto.getParams()));
                    pageInfo = commonReportService.reportHtml(reportDto);
                    html = jasperReportService.reportHtml(reportDto, pageInfo.getRecords(), jasperReport);
                } else {
                    html = jasperReportService.reportHtml(reportDto, null, jasperReport);
                }
                Map result = new HashMap();
                result.put("html", html);
                result.put("pageInfo", pageInfo);
                return R.success(result);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReportException("预览报表异常:" + e.getMessage());
        }
        return R.success();
    }

    @Override
    public boolean isBigData(ReportDto reportDto) {
        handleSql(reportDto);
        int dsId = reportDto.getDsId();
        javax.sql.DataSource ds = DataSourceUtil.getDataSourceByDsId(dsId);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        int count = SqlUtil.count(reportDto.getQuerySql(), jdbcTemplate);
        if (count > 100000) {
            //如果报表数据量大于10w，表示此报表是大数据量报表
            return true;
        }
        return false;
    }

    @Override
    public void exportExcel(ReportDto reportDto, HttpServletResponse response) {
        try {
            handleSql(reportDto);
            //替换sql参数
            if (Report.TYPE_COMMON.equals(reportDto.getType())) {
                commonReportService.exportExcel(reportDto, response);
            } else if (Report.TYPE_JASPER.equals(reportDto.getType())) {
                //获取jasper模板文件地址
                jasperReportService.exportExcel(reportDto, response);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReportException("报表导出excel异常:" + e.getMessage());
        }
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


    private void handleSql(ReportDto reportDto) {
        try {
            if (Report.TYPE_COMMON.equals(reportDto.getType())) {
                reportDto.setQuerySql(replaceParamSql(reportDto.getQuerySql(), reportDto.getParams()));
            } else if (Report.TYPE_JASPER.equals(reportDto.getType())) {
                //获取jasper模板文件地址
                Map<String, Object> filePathResult = jasperReportService.getJasperTemplate(reportDto.getId());
                String jrxmlFilePath = filePathResult.get("jrxmlFilePath").toString();
                JasperReport jasperReport = JasperReportUtil.getJasperReport(jrxmlFilePath);
                String querySql = jasperReport.getQuery().getText();
                reportDto.setQuerySql(replaceParamSql(querySql, reportDto.getParams()));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReportException("处理报表sql异常:" + e.getMessage());
        }
    }

    /**
     * 把参数值替换进sql配置语句中获取到真正执行的sql
     *
     * @param sql
     * @param params
     * @return
     */
    private String replaceParamSql(String sql, List<ReportParamDto> params) {
        //替换sql参数
        for (ReportParamDto param : params) {
            String field = param.getField();
            String $field = "${" + field + "}";
            Object value = param.getValue();
            //最终参数值
            String result = "";
            if (value instanceof List) {
                //如果参数值是数组或者集合需要转换为字符串已逗号分隔
                List<String> valueList = (List<String>) value;
                if (valueList.size() > 0) {
                    result = result + "(";
                    for (String val : valueList) {
                        result = result + val + ",";
                    }
                    result = result.substring(0, result.length() - 1);
                    result = result + ")";
                }
            } else {
                if (value != null) {
                    result = value.toString();
                }
            }

            //处理$ifnull[]，如果参数值为空，删除$ifnull[]包含的内容
            String[] ifnulls = StrUtil.subBetweenAll(sql, "$ifnull[", "]");
            for (String ifnull : ifnulls) {
                if (StrUtil.containsIgnoreCase(ifnull, $field)) {
                    if (StrUtil.isEmpty(result)) {
                        //如果参数值为空,删除$ifnull[]包含的内容
                        sql = sql.replace("$ifnull[" + ifnull + "]", "");
                    } else {
                        sql = sql.replace("$ifnull[" + ifnull + "]", ifnull);
                    }
                }
            }

            sql = sql.replace("${" + field + "}", result);

            parseParam(sql, param.getField(), result);
        }
        return sql;
    }

    /**
     * 替换jasper报表参数
     *
     * @param text
     * @param key
     * @param value
     * @return
     */
    public static String parseParam(String text, String key, String value) {
        String param = "$P!{" + key + "}";
        text = StringUtils.replace(text, param, value);
        String paramLower = "$p!{" + key + "}";
        text = StringUtils.replace(text, paramLower, value);
        String paramNo = "$P{" + key + "}";
        text = StringUtils.replace(text, paramNo, value);
        String paramNOLower = "$p{" + key + "}";
        text = StringUtils.replace(text, paramNOLower, value);
        return text;
    }
}
