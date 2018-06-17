package com.y3tu.tool.web.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.y3tu.tool.web.base.dao.BaseMapper;
import com.y3tu.tool.web.base.service.BaseService;


import java.util.Map;

/**
 * @author y3tu
 * @date 2018/1/29
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public Page<T> queryPage(Page<T> page, Map<String, Object> map) {
        return (Page<T>) baseMapper.queryPage(page,map);
    }
}
