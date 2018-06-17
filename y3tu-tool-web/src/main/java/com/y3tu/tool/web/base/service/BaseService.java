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
     * @param page
     * @param map
     * @return
     */
    Page<T> queryPage(Page<T> page, Map<String, Object> map);
}
