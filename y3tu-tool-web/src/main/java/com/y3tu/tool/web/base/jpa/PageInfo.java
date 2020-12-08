package com.y3tu.tool.web.base.jpa;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Data
public class PageInfo<T>{
    /**
     * 查询条件参数实体
     */
    T entity;

    /**
     * 查询条件参数
     */
    Map params;

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
}