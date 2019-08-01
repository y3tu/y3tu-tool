package com.y3tu.tool.web.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.y3tu.tool.web.base.pojo.PageInfo;

/**
 * @author y3tu
 * <p>
 * 基本Service接口
 */
public interface BaseService<T> extends IService<T> {
    /**
     * 查询分页信息
     *
     * @param pageInfo 分页信息 包含每页多少条,当前页数,排序,查询条件
     * @return
     */
    PageInfo<T> page(PageInfo<T> pageInfo);
}
