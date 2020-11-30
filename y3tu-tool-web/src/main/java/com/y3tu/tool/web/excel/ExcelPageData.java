package com.y3tu.tool.web.excel;

import java.util.List;

/**
 * 分页数据导出到Excel需要实现此方法
 * 在此方法中实现分页数据的查询
 *
 * @param <E>
 * @author y3tu
 */
@FunctionalInterface
public interface ExcelPageData<E> {
    /**
     * 分页获取数据
     *
     * @param startNum 从第几条数据开始
     * @param pageSize 分页大小
     * @return
     */
    List<E> queryDataByPage(int startNum, int pageSize);
}
