package com.y3tu.tool.web.base.controller;


import com.y3tu.tool.web.base.entity.BaseEntity;
import com.y3tu.tool.web.base.pojo.PageInfo;
import com.y3tu.tool.web.base.pojo.Query;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.base.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller基类
 *
 * @author y3tu
 */
public class BaseController<T extends BaseService, M extends BaseEntity> {

    @Autowired
    public T service;

    /**
     * 分页查询
     *
     * @param params 查询参数 包含分页信息和查询条件
     *               前台传入参数params封装规则
     *               page：当前页
     *               limit：每页条数
     *               order：排序
     *               查询条件的key要和mapper.xml文件里面的key保持一致
     * @return
     */
    @RequestMapping(value = "/getByPage", method = RequestMethod.GET)
    @ResponseBody
    public R getByPage(@RequestParam Map<String, Object> params) {
        PageInfo pageInfo = new PageInfo(service.queryPage(new Query<M>(params).getPage(), params));
        return R.ok(pageInfo);
    }

    /**
     * 获取全部数据
     *
     * @return
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public R getAll() {
        return R.ok(service.selectList(null));
    }

    /**
     * 通过id获取
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public R get(@PathVariable String id) {
        return R.ok(service.selectById(id));
    }

    /**
     * 保存数据
     *
     * @param entity 保存的数据
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public R save(@RequestParam M entity) {
        service.insert(entity);
        return R.ok("保存成功!");
    }

    /**
     * 更新数据
     *
     * @param entity 更新的数据
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ResponseBody
    public R update(@RequestParam M entity) {
        service.updateById(entity);
        return R.ok("更新成功!");
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return
     */
    @RequestMapping(value = "/delById/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public R delById(@PathVariable String id) {
        service.deleteById(id);
        return R.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return
     */
    @RequestMapping(value = "/delByIds", method = RequestMethod.DELETE)
    @ResponseBody
    public R delByIds(@RequestParam List<String> ids) {
        service.deleteBatchIds(ids);
        return R.ok("删除成功!");
    }

    /**
     * 重定向至地址 url
     *
     * @param url 请求地址
     * @return
     */
    protected String redirectTo(String url) {
        StringBuffer rto = new StringBuffer("redirect:");
        rto.append(url);
        return rto.toString();
    }


}
