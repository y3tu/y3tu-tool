package com.y3tu.tool.web.base.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

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
     * @param page 分页信息 包含每页多少条,当前页数，排序
     * @param map  查询条件
     * @return
     */
    Page<T> queryPage(Page<T> page, Map<String, Object> map);
}
