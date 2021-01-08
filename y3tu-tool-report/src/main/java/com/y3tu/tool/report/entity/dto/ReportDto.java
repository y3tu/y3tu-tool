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

    Integer id;
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
    String status;
    /**
     * 报表类型
     */
    String type;
    /**
     * 数据源ID
     */
    Integer dsId;
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
