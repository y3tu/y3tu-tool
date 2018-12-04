package com.y3tu.tool.web.base.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
     * @param page     分页的条件
     * @param queryMap 实体对象的查询条件
     * @return
     */
    Page<T> queryPage(Page page, Map<String, Object> queryMap);

    /**
     * 根据查询条件查询表中记录 不分页，查全量数据
     *
     * @param queryMap
     * @return
     */
    List<T> queryPage(Map<String, Object> queryMap);

}
