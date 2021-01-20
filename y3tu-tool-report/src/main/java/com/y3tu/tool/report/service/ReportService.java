package com.y3tu.tool.report.service;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.report.entity.domain.Report;
import com.y3tu.tool.report.entity.dto.ReportDto;
import com.y3tu.tool.report.entity.dto.ReportParamDto;
import com.y3tu.tool.web.base.jpa.BaseService;
import com.y3tu.tool.web.base.jpa.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author y3tu
 */
public interface ReportService extends BaseService<Report> {
    /**
     * 创建报表
     *
     * @param reportDto
     */
    void createReport(ReportDto reportDto);

    /**
     * 更新报表
     *
     * @param reportDto
     */
    void updateReport(ReportDto reportDto);

    /**
     * 删除报表
     *
     * @param reportId
     */
    void deleteReport(int reportId);

    /**
     * 下载报表附件
     *
     * @param reportId 报表id
     * @param request
     * @param response
     */
    void download(int reportId, HttpServletRequest request, HttpServletResponse response);

    /**
     * 预览
     *
     * @param reportId
     * @return
     */
    R preview(int reportId);

    /**
     * 解析sql
     *
     * @param sql  sql
     * @param dsId 数据源
     * @return
     */
    R parseSql(String sql, int dsId);

    /**
     * 查询报表数据
     *
     * @param sql      查询sql
     * @param dsId     数据源
     * @param params   查询参数
     * @param pageInfo 分页信息
     * @return
     */
    R queryTableData(String sql, int dsId, List<ReportParamDto> params, PageInfo pageInfo);
}
