package com.y3tu.tool.server.report.service.impl;

import com.y3tu.tool.core.io.FileUtil;
import com.y3tu.tool.server.report.entity.domain.ReportAttachment;
import com.y3tu.tool.server.report.exception.ReportException;
import com.y3tu.tool.server.report.repository.ReportAttachmentRepository;
import com.y3tu.tool.server.report.service.ReportAttachmentService;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import com.y3tu.tool.web.file.util.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author y3tu
 */
@Service
@Slf4j
public class ReportAttachmentServiceImpl extends BaseServiceImpl<ReportAttachmentRepository, ReportAttachment> implements ReportAttachmentService {
    @Override
    public boolean upload(String fileTempPrefix, MultipartFile file) {
        String uploadLocalPath = FileUtil.SYS_TEM_DIR + File.separator + fileTempPrefix + ".jrxml";
        //上传本地临时目录
        boolean flag = UploadUtil.upload(file, uploadLocalPath);
        //编译模板
        String jasperFile = FileUtil.SYS_TEM_DIR + File.separator + fileTempPrefix + ".jasper";
        try {
            JasperCompileManager.compileReportToFile(uploadLocalPath, jasperFile);
            //删除临时编译后的文件
            FileUtil.del(jasperFile);
        } catch (JRException e) {
            log.error(e.getMessage(), e);
            throw new ReportException("上传失败！模板编译异常:" + e.getMessage());
        }
        return flag;
    }

    @Override
    public List<ReportAttachment> getByReportId(int reportId) {
        return repository.getByReportId(reportId);
    }

    @Override
    public void deleteByReportId(int reportId) {
        repository.deleteByReportId(reportId);
    }
}
