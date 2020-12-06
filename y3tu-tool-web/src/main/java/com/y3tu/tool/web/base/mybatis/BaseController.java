package com.y3tu.tool.web.base.mybatis;

import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.pojo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller基类
 * 如果继承此BaseController，那么会继承此类的方法，提供了简单的增删改成，但是无法做到权限控制；
 * 也可不继承BaseController，直接复制里面的方法在自己的Controller里面，根据需求自行添加内容
 *
 * @author y3tu
 */
public abstract class BaseController<K extends BaseService<T>, T extends BaseEntity> {

    @Autowired
    protected K service;

    /**
     * 分页查询
     *
     * @param page 查询参数 包含分页信息和查询条件
     *             前台传入参数params封装规则
     *             current：当前页
     *             size：每页条数
     *             ascs:升序排列的字段 字符串数组
     *             descs:降序排列的字段 字符串数组
     *             查询条件的key要和mapper.xml文件里面的key保持一致
     * @return 分页数据
     */
    @PostMapping
    public R page(@RequestBody Page<T> page) {
        return R.success(service.page(page));
    }

    /**
     * 获取全部数据
     *
     * @return 全部数据
     */
    @GetMapping
    public R getAll() {
        return new R(service.list(null));
    }

    /**
     * 通过主键id获取
     *
     * @param id 主键
     * @return 数据
     */
    @GetMapping(value = "/get/{id}")
    public R get(@PathVariable String id) {
        return R.success(service.getById(id));
    }

    /**
     * 保存数据
     *
     * @param entity 保存的数据
     * @return 响应
     */
    @PostMapping
    public R save(@RequestBody T entity) {
        if (service.save(entity)) {
            return R.success(entity);
        } else {
            return R.error();
        }
    }

    /**
     * 更新数据
     *
     * @param entity 更新的数据
     * @return 响应
     */
    @PutMapping
    public R update(@RequestBody T entity) {
        if (service.updateById(entity)) {
            return R.success();
        } else {
            return R.error();
        }
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 响应
     */
    @DeleteMapping(value = "/delById/{id}")
    public R delById(@PathVariable String id) {
        if (service.removeById(id)) {
            return R.success();
        } else {
            return R.error();
        }
    }

    /**
     * 批量删除
     *
     * @param ids 主键集合
     * @return 响应
     */
    @DeleteMapping(value = "/delByIds/{ids}")
    public R delByIds(@PathVariable String[] ids) {
        if (service.removeByIds(CollectionUtil.toList(ids))) {
            return R.success();
        } else {
            return R.error();
        }
    }

    /**
     * 重定向至地址 url
     *
     * @param url 请求地址
     * @return 重定向url
     */
    protected String redirectTo(String url) {
        return "redirect:" + url;
    }


}
