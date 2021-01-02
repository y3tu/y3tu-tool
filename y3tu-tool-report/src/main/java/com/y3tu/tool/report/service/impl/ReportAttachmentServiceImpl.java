package com.y3tu.tool.report.service.impl;

import com.y3tu.tool.report.entity.domain.ReportAttachment;
import com.y3tu.tool.report.repository.ReportAttachmentRepository;
import com.y3tu.tool.report.service.ReportAttachmentService;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author y3tu
 */
@Service
public class ReportAttachmentServiceImpl extends BaseServiceImpl<ReportAttachmentRepository, ReportAttachment> implements ReportAttachmentService {
}
