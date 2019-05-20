package com.y3tu.tool.web.base.mapper;


import com.y3tu.tool.web.base.pojo.PageInfo;

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
    PageInfo<T> page(PageInfo page);


}
