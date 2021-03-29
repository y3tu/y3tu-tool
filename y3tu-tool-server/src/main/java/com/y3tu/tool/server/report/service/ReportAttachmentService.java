package com.y3tu.tool.server.report.service;

import com.y3tu.tool.server.report.entity.domain.ReportAttachment;
import com.y3tu.tool.web.base.jpa.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author y3tu
 */
public interface ReportAttachmentService extends BaseService<ReportAttachment> {
    /**
     * 上传文件到程序运行服务器临时目录
     *
     * @param fileTempPrefix 文件临时前缀
     * @param file
     * @return true 上传成功 false 上传失败
     */
    boolean upload(String fileTempPrefix, MultipartFile file);

    /**
     * 根据报表id获取附件记录
     *
     * @param reportId
     */
    List<ReportAttachment> getByReportId(int reportId);

    /**
     * 根据报表id删除附件记录
     *
     * @param reportId
     */
    void deleteByReportId(int reportId);
}
