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

    String type;

    String dictCode;

}
