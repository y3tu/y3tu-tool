package com.y3tu.tool.web.base.mybatis;

import java.util.List;
import java.util.Map;

/**
 * Mapper接口
 *
 * @author y3tu
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

    /**
     * 根据查询条件查询表中记录并分页
     *
     * @param page 分页信息和查询条件
     * @return
     */
    Page<T> page(Page page);

    /**
     * 根据条件查询数据 此方法不分页 和分页方法是相同的查询sql
     * @param params
     * @return
     */
    List<T> page(Map params);

}
