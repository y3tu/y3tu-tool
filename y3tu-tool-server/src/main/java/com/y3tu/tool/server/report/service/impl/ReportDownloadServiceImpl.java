package com.y3tu.tool.server.report.service.impl;

import cn.hutool.core.util.ZipUtil;
import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.server.report.configure.ReportProperties;
import com.y3tu.tool.server.report.entity.domain.Report;
import com.y3tu.tool.server.report.entity.domain.ReportDownload;
import com.y3tu.tool.server.report.entity.dto.ReportDto;
import com.y3tu.tool.server.report.entity.dto.ReportParamDto;
import com.y3tu.tool.server.report.exception.ReportException;
import com.y3tu.tool.server.report.repository.ReportDownloadRepository;
import com.y3tu.tool.server.report.service.ReportDownloadService;
import com.y3tu.tool.server.report.service.ReportService;
import com.y3tu.tool.server.websocket.MessageEndPoint;
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
import java.nio.charset.StandardCharsets;
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
    @Autowired
    MessageEndPoint messageEndPoint;

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
            String tempFileName = UUID.randomUUID().toString();
            String tempFileNameExcel = tempFileName + ".xlsx";
            String tempFileNameZip = tempFileName + ".zip";
            String tempFilePathExcel = FileUtil.SYS_TEM_DIR + File.separator + tempFileNameExcel;
            String tempFilePathZip = FileUtil.SYS_TEM_DIR + File.separator + tempFileNameZip;
            File file = new File(tempFilePathExcel);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //生成报表文件
            reportService.exportExcel(reportDto, fileOutputStream);
            //压缩报表文件
            ZipUtil.zip(tempFilePathExcel, tempFilePathZip, StandardCharsets.UTF_8, false);
            //上传到远程服务器上
            boolean flag = remoteFileHelper.upload(properties.getReportRemotePath(), tempFileNameZip, tempFilePathZip);
            //更新report_download表状态
            if (flag) {
                //删除本地服务器报表压缩文件
                FileUtil.del(tempFilePathZip);
                reportDownload.setRemoteFilePath(properties.getReportRemotePath() + tempFileNameZip);
                reportDownload.setRealFileName(tempFileName);
                reportDownload.setStatus(ReportDownload.STATUS_NORMAL);
                reportDownload.setUpdateTime(new Date());
                this.update(reportDownload);
                messageEndPoint.sendAllMessage("报表" + reportDto.getName() + "生成完成！");
            } else {
                reportDownload.setStatus(ReportDownload.STATUS_ERROR);
                reportDownload.setErrMsg("上传远程服务器失败！");
                reportDownload.setUpdateTime(new Date());
                this.update(reportDownload);
            }
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
        String filePath = FileUtil.SYS_TEM_DIR + File.separator + reportDownload.getRealFileName() + ".xlsx";
        File file = new File(filePath);
        if (!FileUtil.exist(file)) {
            //如果不在服务器临时目录，从远处服务器中下载
            String filePathZip = FileUtil.SYS_TEM_DIR + File.separator + reportDownload.getRealFileName() + ".zip";
            boolean flag = remoteFileHelper.download(reportDownload.getRemoteFilePath(), filePathZip);
            if (flag) {
                //解压报表文件
                ZipUtil.unzip(filePathZip, FileUtil.SYS_TEM_DIR + File.separator, StandardCharsets.UTF_8);
                //删除压缩文件
                FileUtil.del(filePathZip);
                //更新下载次数
                reportDownload.setDownloadTimes(reportDownload.getDownloadTimes() + 1);
                this.update(reportDownload);
                //下载文件到浏览器
                FileUtil.downloadFile(file, report.getName(), false, request, response);
            } else {
                reportDownload.setErrMsg("远程下载报表文件异常!");
                reportDownload.setUpdateTime(new Date());
                reportDownload.setStatus(ReportDownload.STATUS_ERROR);
                this.update(reportDownload);
                throw new ReportException("远程下载报表文件异常!");
            }
        } else {
            reportDownload.setDownloadTimes(reportDownload.getDownloadTimes() + 1);
            this.update(reportDownload);
            FileUtil.downloadFile(file, report.getName(), false, request, response);
        }
    }
}
