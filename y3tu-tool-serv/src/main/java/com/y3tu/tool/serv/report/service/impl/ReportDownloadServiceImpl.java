package com.y3tu.tool.serv.report.service.impl;

import com.y3tu.tool.serv.report.entity.domain.ReportDownload;
import com.y3tu.tool.serv.report.entity.dto.ReportDownloadDto;
import com.y3tu.tool.serv.report.repository.ReportDownloadRepository;
import com.y3tu.tool.serv.report.service.ReportDownloadService;
import com.y3tu.tool.web.base.jpa.BaseServiceImpl;
import com.y3tu.tool.web.base.jpa.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author y3tu
 */
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@Service
public class ReportDownloadServiceImpl extends BaseServiceImpl<ReportDownloadRepository, ReportDownload> implements ReportDownloadService {

    @Override
    public PageInfo page(PageInfo pageInfo) {
        String reportName = pageInfo.getParams().getOrDefault("reportName","").toString();
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
        Page<ReportDownloadDto> page = repository.getReportDownloadByPage(reportName,pageable);
        pageInfo.setRecords(page.getContent());
        pageInfo.setTotal(page.getTotalElements());
        return pageInfo;
    }

    @Override
    public List<ReportDownload> getByReportId(int reportId) {
        List<String> statusList = new ArrayList<>();
        statusList.add(ReportDownload.STATUS_NORMAL);
        statusList.add(ReportDownload.STATUS_WAITING);
        return repository.getByReportIdAndStatusIsIn(reportId,statusList);
    }
}
