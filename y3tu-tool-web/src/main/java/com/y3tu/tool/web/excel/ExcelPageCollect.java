package com.y3tu.tool.web.excel;

import java.util.List;

/**
 * 分页数据导出到Excel需要实现此方法
 * 在此方法中实现分页数据的查询
 * @param <E>
 * @author y3tu
 */
@FunctionalInterface
public interface ExcelPageCollect<E> {

    /**
     * @param current 当前页，从 1 开始
     * @param size 分页大小
     * @return
     */
    List<E> data(int current, int size);
}
