package com.y3tu.tool.web.sql;

import java.util.List;

/**
 * 数据处理接口
 *
 * @author y3tu
 */
@FunctionalInterface
public interface DataHandler<T> {

    /**
     * 处理数据
     *
     * @param dataList
     */
    void handle(List<T> dataList);

}
