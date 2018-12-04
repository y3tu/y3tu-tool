package com.y3tu.tool.web.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.y3tu.tool.web.base.pojo.PageInfo;

import java.util.Map;

/**
 * @author y3tu
 * <p>
 * 基本Service接口
 */
public interface BaseService<T> extends IService<T> {
    /**
     * 查询分页信息
     *
     * @param pageInfo 分页信息 包含每页多少条,当前页数，排序
     * @param map  查询条件
     * @return
     */
    PageInfo<T> queryPage(PageInfo<T> pageInfo, Map<String, Object> map);
}
