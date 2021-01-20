package com.y3tu.tool.report.entity.dto;

import com.y3tu.tool.web.base.jpa.PageInfo;
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
    String tableHeader;
    /**
     * 查询sql
     */
    String querySql;
    /**
     * 报表参数
     */
    List<ReportParamDto> params;

    /**
     * 附件名称
     */
    String fileName;
    /**
     * 附件临时名称前缀
     */
    String fileTempPrefix;

    /**
     * 查询分页参数
     */
    PageInfo pageInfo;
}
