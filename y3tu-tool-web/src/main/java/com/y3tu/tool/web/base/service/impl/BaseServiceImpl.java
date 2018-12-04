package com.y3tu.tool.web.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.y3tu.tool.web.base.mapper.BaseMapper;
import com.y3tu.tool.web.base.pojo.PageInfo;
import com.y3tu.tool.web.base.service.BaseService;


import java.util.List;
import java.util.Map;

/**
 * @author y3tu
 * @date 2018/1/29
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public PageInfo<T> queryPage(PageInfo<T> pageInfo, Map<String, Object> map) {
        if (pageInfo != null) {
            //表示分页
            baseMapper.queryPage(pageInfo.getPage(), map);
            pageInfo.setCurrentPage(pageInfo.getPage().getCurrent());
            pageInfo.setList(pageInfo.getPage().getRecords());
            pageInfo.setTotalCount(pageInfo.getPage().getTotal());
            pageInfo.setTotalPage(pageInfo.getPage().getPages());

        } else {
            //不分页，查全量数据
            List<T> list = baseMapper.queryPage(map);
            pageInfo.setTotalCount(list.size());
            pageInfo.setList(list);
        }
        return pageInfo;
    }
}
