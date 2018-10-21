package com.y3tu.tool.web.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.y3tu.tool.web.base.mapper.BaseMapper;
import com.y3tu.tool.web.base.service.BaseService;


import java.util.List;
import java.util.Map;

/**
 * @author y3tu
 * @date 2018/1/29
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public Page<T> queryPage(Page<T> page, Map<String, Object> map) {
        if (page != null) {
            //表示分页
            page.setRecords(baseMapper.queryPage(page, map));
        } else {
            //不分页，查全量数据
            page = new Page<T>();
            List<T> list = baseMapper.queryPage(map);
            page.setTotal(list.size());
            page.setRecords(list);
        }
        return page;
    }
}
