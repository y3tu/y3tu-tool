package com.y3tu.tool.report.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * 报表创建实体
 *
 * @author y3tu
 */
@Data
public class ReportDto {
    /**
     * 报表名
     */
    String name;
    /**
     * 备注
     */
    String remarks;
    /**
     * 状态
     */
    int status;
    /**
     * 报表类型
     */
    int type;
    /**
     * 数据源ID
     */
    long dsId;
    /**
     * 表头
     */
    String columnHeader;
    /**
     * 查询sql
     */
    String querySql;

    List<ReportParamDto> params;
}
