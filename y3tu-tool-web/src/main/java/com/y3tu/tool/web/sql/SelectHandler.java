package com.y3tu.tool.web.sql;

import java.util.List;

/**
 * @author y3tu
 * @date 2020/10/22
 */
@FunctionalInterface
public interface SelectHandler<T> {
    public List<T> handle(List<T> dataList);
}
