package com.y3tu.tool.serv.report.service.impl;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.serv.report.configure.ReportProperties;
import com.y3tu.tool.serv.report.entity.domain.Report;
import com.y3tu.tool.serv.report.entity.domain.ReportDownload;
import com.y3tu.tool.serv.report.entity.dto.ReportDto;
import com.y3tu.tool.serv.report.entity.dto.ReportParamDto;
import com.y3tu.tool.serv.report.exception.ReportException;
import com.y3tu.tool.serv.report.repository.ReportDownloadRepository;
import com.y3tu.tool.serv.report.service.ReportDownloadService;
import com.y3tu.tool.serv.report.service.ReportService;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import com.y3tu.tool.web.base.jpa.PageInfo;
import com.y3tu.tool.web.file.service.RemoteFileHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author y3tu
 */
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Service
public class ReportDownloadServiceImpl extends BaseServiceImpl<ReportDownloadRepository, ReportDownload> implements ReportDownloadService {

    @Autowired
    ReportService reportService;
    @Autowired
    RemoteFileHelper remoteFileHelper;
    @Autowired
    ReportProperties properties;

    @Override
    public PageInfo page(PageInfo pageInfo) {
        String reportName = pageInfo.getParams().getOrDefault("reportName", "").toString();
        //排序
        List<String> ascArr = pageInfo.getAsc();
        List<String> descArr = pageInfo.getDesc();
        List<Sort.Order> orderList = new ArrayList<>();
        if (ascArr != null) {
            for (String asc : ascArr) {
                Sort.Order order = new Sort.Order(Sort.Direction.ASC, asc);
                orderList.add(order);
            }
        }
        if (descArr != null) {
            for (String desc : descArr) {
                Sort.Order order = new Sort.Order(Sort.Direction.DESC, desc);
                orderList.add(order);
            }
        }
        //前台传入current是从1开始的，后台是从0开始的，需要减1
        PageRequest pageable = PageRequest.of(pageInfo.getCurrent() - 1, pageInfo.getSize(), Sort.by(orderList));
        Page<List<Map<String, Object>>> page = repository.getReportDownloadByPage(reportName, pageable);
        pageInfo.setRecords(page.getContent());
        pageInfo.setTotal(page.getTotalElements());
        return pageInfo;
    }

    @Override
    public List<ReportDownload> getByReportId(int reportId) {
        List<String> statusList = new ArrayList<>();
        statusList.add(ReportDownload.STATUS_NORMAL);
        statusList.add(ReportDownload.STATUS_WAIT);
        return repository.getByReportIdAndStatusIsIn(reportId, statusList);
    }

    @Override
    public List<ReportDownload> getWaitData() {
        return repository.getWaitData();
    }

    @Override
    public void handleDownload(ReportDownload reportDownload) {
        try {
            int reportId = reportDownload.getReportId();
            Report report = reportService.getById(reportId);
            String paramJson = reportDownload.getParamJson();
            List<ReportParamDto> params = JsonUtil.parseArray(paramJson, ReportParamDto.class);
            ReportDto reportDto = new ReportDto();
            BeanUtils.copyProperties(report, reportDto);
            reportDto.setParams(params);

            //在服务器临时目录上生成报表文件
            String tempFileName = UUID.randomUUID().toString() + ".xlsx";
            String tempFilePath = FileUtil.SYS_TEM_DIR + File.separator + tempFileName;
            File file = new File(tempFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //生成报表文件
            reportService.exportExcel(reportDto, fileOutputStream);
            //上传到远程服务器上
            boolean flag = remoteFileHelper.upload(properties.getRemotePath(), tempFileName, tempFilePath);
            //更新report_download表状态
            if (flag) {
                reportDownload.setRemoteFilePath(properties.getRemotePath() + tempFileName);
            }
            reportDownload.setRealFileName(tempFileName);
            reportDownload.setStatus(ReportDownload.STATUS_NORMAL);
            reportDownload.setUpdateTime(new Date());
            this.update(reportDownload);

        } catch (Exception e) {
            reportDownload.setErrMsg(e.getMessage());
            reportDownload.setStatus(ReportDownload.STATUS_ERROR);
            reportDownload.setUpdateTime(new Date());
            this.update(reportDownload);
            log.error(e.getMessage(), e);
            throw new ReportException("报表生成失败!:" + e.getMessage());
        }

    }

    @Override
    public void download(int id, HttpServletRequest request, HttpServletResponse response) {
        ReportDownload reportDownload = this.getById(id);
        Report report = reportService.getById(reportDownload.getReportId());
        if (!reportDownload.getStatus().equals(ReportDownload.STATUS_NORMAL)) {
            throw new ReportException("报表还未生成好，请稍后下载！");
        }
        //先判断报表文件是否已经在服务器临时目录
        String filePath = FileUtil.SYS_TEM_DIR + File.separator + reportDownload.getRealFileName();
        File file = new File(filePath);
        if (!FileUtil.exist(file)) {
            boolean flag = remoteFileHelper.download(reportDownload.getRemoteFilePath(), filePath);
            if (flag) {
                reportDownload.setDownloadTimes(reportDownload.getDownloadTimes() + 1);
                this.update(reportDownload);
                FileUtil.downloadFile(file, report.getName(), false, request, response);
            } else {
                throw new ReportException("远程下载报表文件异常");
            }
        } else {
            reportDownload.setDownloadTimes(reportDownload.getDownloadTimes() + 1);
            this.update(reportDownload);
            FileUtil.downloadFile(file, report.getName(), false, request, response);
        }
    }
}
