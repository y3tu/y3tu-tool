package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.report.configure.ToolReportProperties;
import com.y3tu.tool.report.entity.domain.Report;
import com.y3tu.tool.report.entity.domain.ReportAttachment;
import com.y3tu.tool.report.entity.domain.ReportParam;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.report.exception.ReportException;
import com.y3tu.tool.report.repository.ReportRepository;
import com.y3tu.tool.report.service.ReportAttachmentService;
import com.y3tu.tool.report.service.ReportParamService;
import com.y3tu.tool.report.service.ReportService;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import com.y3tu.tool.web.file.service.RemoteFileHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

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
            String tempFilePath = FileUtil.SYS_TEM_DIR + reportDto.getFileTempPrefix() + ".jrxml";
            String tempFileName = reportDto.getFileTempPrefix() + ".jrxml";
            boolean flag = remoteFileHelper.upload(properties.getRemotePath(), tempFileName, tempFilePath);
            //删除临时目录文件
            FileUtil.del(tempFilePath);
            if (flag) {
                //文件上传成功
                ReportAttachment reportAttachment = new ReportAttachment();
                reportAttachment.setReportId(report.getId());
                reportAttachment.setStatus("00A");
                reportAttachment.setName(reportDto.getFileName());
                reportAttachment.setPath(properties.getRemotePath() + reportDto.getFileTempPrefix() + ".jrxml");
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
        //处理报表模板附件
        if (StrUtil.isNotEmpty(reportDto.getFileName())) {
            //附件上传到远程服务器
            String tempFilePath = FileUtil.SYS_TEM_DIR + reportDto.getFileTempPrefix() + ".jrxml";
            String tempFileName = reportDto.getFileTempPrefix() + ".jrxml";
            boolean flag = remoteFileHelper.upload(properties.getRemotePath(), tempFileName, tempFilePath);
            //删除临时目录文件
            FileUtil.del(tempFilePath);
            if (flag) {
                //文件上传成功 更新记录
                List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(report.getId());
                for (ReportAttachment reportAttachment : reportAttachmentList) {
                    String oldPath = reportAttachment.getPath();
                    reportAttachment.setName(reportDto.getFileName());
                    reportAttachment.setPath(properties.getRemotePath() + reportDto.getFileTempPrefix() + ".jrxml");
                    reportAttachment.setUpdateTime(new Date());
                    reportAttachmentService.update(reportAttachment);
                    //删除老附件
                    if (!remoteFileHelper.remove(oldPath)) {
                        throw new ReportException("删除远程服务器上的附件失败！");
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
            if (!remoteFileHelper.remove(reportAttachment.getPath())) {
                throw new ReportException("删除远程服务器上的附件失败！");
            }
        }
    }

    @Override
    public void download(int reportId, HttpServletRequest request, HttpServletResponse response) {
        List<ReportAttachment> reportAttachmentList = reportAttachmentService.getByReportId(reportId);
        for (ReportAttachment reportAttachment : reportAttachmentList) {
            remoteFileHelper.download(reportAttachment.getPath(), reportAttachment.getName(), false, request, response);
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
}
