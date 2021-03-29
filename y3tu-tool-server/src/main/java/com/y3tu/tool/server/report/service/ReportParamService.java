package com.y3tu.tool.server.report.service;

import com.y3tu.tool.server.report.entity.domain.ReportParam;
import com.y3tu.tool.web.base.jpa.BaseService;

import java.util.List;

/**
 * @author y3tu
 */
public interface ReportParamService extends BaseService<ReportParam> {
    /**
     * 根据reportId获取报表参数
     *
     * @param reportId
     * @return
     */
    List<ReportParam> getByReportId(int reportId);

    /**
     * 根据reportId删除参数
     *
     * @param reportId
     */
    void deleteByReportId(int reportId);
}
