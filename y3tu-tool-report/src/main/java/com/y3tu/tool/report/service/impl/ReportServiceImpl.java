package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.report.configure.ToolReportProperties;
import com.y3tu.tool.report.entity.domain.Report;
import com.y3tu.tool.report.entity.domain.ReportAttachment;
import com.y3tu.tool.report.entity.domain.ReportParam;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.report.entity.dto.ReportParamDto;
import com.y3tu.tool.report.exception.ReportException;
import com.y3tu.tool.report.repository.ReportRepository;
import com.y3tu.tool.report.service.CommonReportService;
import com.y3tu.tool.report.service.ReportAttachmentService;
import com.y3tu.tool.report.service.ReportParamService;
import com.y3tu.tool.report.service.ReportService;
import com.y3tu.tool.report.util.DataSourceUtil;
import com.y3tu.tool.report.util.JasperReportUtil;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import com.y3tu.tool.web.file.service.RemoteFileHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    public void download(int reportId, HttpServletRequest request, HttpServletResponse response) {
        List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(reportId);
        for (ReportAttachment reportAttachment : reportAttachmentList) {
            remoteFileHelper.download(reportAttachment.getRemoteFilePath(), reportAttachment.getName(), false, request, response);
        }
    }

    @Override
    public R queryReportData(ReportDto reportDto, HttpServletResponse response) {
        try {
            if (Report.TYPE_COMMON.equals(reportDto.getType())) {
                //通用报表
                return commonReportService.queryReportData(reportDto);
            } else if (Report.TYPE_JASPER.equals(reportDto.getType())) {
                //Jasper报表
                //获取jasper模板文件地址
                Map<String, Object> filePathResult = getJasperTemplate(reportDto.getId());
                //获取报表配置的数据源连接
                javax.sql.DataSource dataSource = DataSourceUtil.getDataSourceByDsId(reportDto.getDsId());
                //报参数转换成map
                Map<String, Object> paramMap = new HashMap<>();
                List<ReportParamDto> params = reportDto.getParams();
                for (ReportParamDto paramDto : params) {
                    paramMap.put(paramDto.getField(), paramDto.getValue());
                }
                paramMap.put("PAGE_NUMBER",1);
                paramMap.put("PAGE_COUNT",20);
                JasperPrint jasperPrint = JasperReportUtil.getJasperPrint(filePathResult.get("jasperFilePath").toString(), dataSource.getConnection(), paramMap);
                //删除最后空白页
                int pageCount = jasperPrint.getPages().size() - 1;
                if (pageCount >= 0) {
                    if (jasperPrint.getPages().get(pageCount).getElements().size() == 0) {
                        jasperPrint.removePage(pageCount);
                    }
                }

                String html = JasperReportUtil.exportToHtml(jasperPrint);
                return R.success(html);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReportException("预览报表异常:" + e.getMessage());
        }
        return R.success();
    }


    private Map<String, Object> getJasperTemplate(int reportId) {
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
