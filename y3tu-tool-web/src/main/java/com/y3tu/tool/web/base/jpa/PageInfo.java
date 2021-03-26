package com.y3tu.tool.web.base.jpa;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 分页实体
 *
 * @author y3tu
 */
@Data
public class PageInfo<T> {
    /**
     * 查询条件参数实体
     */
    T entity;

    /**
     * 查询条件参数
     */
    Map<String,Object> params;

    /**
     * 查询数据列表
     */
    private List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    private long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    private int size = 10;

    /**
     * 当前页
     */
    private int current = 1;

    /**
     * 正序字段
     */
    private List<String> asc;

    /**
     * 降序字段
     */
    private List<String> desc;
}
