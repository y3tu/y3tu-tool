package com.y3tu.tool.report.entity.dto;

import lombok.Data;

/**
 * 参数实体
 *
 * @author y3tu
 */
@Data
public class ReportParamDto {

    Integer id;

    Integer seq;

    Integer reportId;

    String name;

    String field;

    String relation;

    Integer type;

    String dictCode;

    /**
     * 前台输入的参数值
     */
    Object value;

}
