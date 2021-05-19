package com.y3tu.tool.lowcode.report.repository;

import com.y3tu.tool.lowcode.report.entity.domain.Report;
import com.y3tu.tool.web.base.jpa.BaseRepository;

/**
 * @author y3tu
 */
public interface ReportRepository extends BaseRepository<Report> {
    /**
     * 根据报表名称获取报表
     *
     * @param name
     * @return
     */
    Report getByName(String name);
}
